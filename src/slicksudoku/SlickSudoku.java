package slicksudoku;

import java.awt.Font;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


public class SlickSudoku extends BasicGame {

	public static final int DISPLAY_WIDTH = 800;
	public static final int DISPLAY_HEIGHT = 600;

	private Board board = null;
	private int selX = 4;
	private int selY = 4;
	private int selInterval = 20;
	
	private Button restartButton = null;
	private Button exitButton = null;
	
	private boolean SUCCESSFLAG = false;
	private Input input = null;

	private Font font = new Font("Times New Roman", Font.BOLD, 50);
	private TrueTypeFont successFont = null;
	private Font font2 = new Font("Times New Roman", Font.PLAIN, 20);
	private TrueTypeFont enterFont = null;
	private int startSlideX = 0;
	private int slideX = 50;
	private int strInterval = 30;
	private boolean strSwitch = true;

	private int searchX = -1, searchY = -1;
	private boolean searchFlag = true;

	private int[] mousePoint = new int[2];

	private int[] numpad = {Input.KEY_NUMPAD1, Input.KEY_NUMPAD2, Input.KEY_NUMPAD3,
					Input.KEY_NUMPAD4, Input.KEY_NUMPAD5, Input.KEY_NUMPAD6,
					Input.KEY_NUMPAD7, Input.KEY_NUMPAD8, Input.KEY_NUMPAD9	};

	private NumBoard numBoard = null;
	
	private Image backgroundImage = null;
	//private Image exitImage = null;
	
	private SudokuWatch timer = null;
	private Font font3 = new Font("Times New Roman", Font.BOLD, 50);
	private TrueTypeFont timerFont = null;
	
	private int count;
	private boolean countFlag;
	private boolean timerFlag;
	
	private int prevMouseX = 0;
	private int prevMouseY = 0;
	private int nowMouseY = 0;
	private int nowMouseX = 0;
	
	private boolean READYFLAG = true;
	
	private Font font4 = new Font("Times New Roman", Font.PLAIN, 30);
	private TrueTypeFont readyFont = null;
	
	private Font font5 = new Font("Serif", Font.PLAIN, 20);
	private UnicodeFont readyFont2 = new UnicodeFont(font5);
	private String readyText = "Enterキーまたはクリックでスタート、上下で難易度を設定";
	
	private Font font6 = new Font("Serif", Font.PLAIN, 15);
	private UnicodeFont dabunFont = new UnicodeFont(font6);
	private String dabunText = "※画面は開発中にやる気を失ったものです";
	
	private int questionLevel;
	private boolean SUCCESSFLAG_ENTRY;
	private int successInterval;
	

	public SlickSudoku() {
		super("Slick Sudoku");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		input = gc.getInput();
		board = new Board();
		numBoard = new NumBoard(input);
		restartButton = new Button(600, 450, 100, 80, "ReStart\n(F12)");
		exitButton = new Button(700, 500, 60, 30, "Exit");
		gc.setTargetFrameRate(60);
		
		DataRoader roader = new DataRoader();
		try {
			backgroundImage = new Image(roader.getFilePath("background"));
			//exitImage = new Image("data/img/exit.png");
			
		} catch (RuntimeException e) {
			backgroundImage = new Image(0, 0);
		}
		
		successFont = new TrueTypeFont(font, true);
		enterFont = new TrueTypeFont(font2,true);
		timerFont = new TrueTypeFont(font3, true);
		readyFont = new TrueTypeFont(font4, true);
		
//		Question question = new Question(5);
//		board.setQuestion(question);
		
		numBoard.init();

		READYFLAG = true;
		SUCCESSFLAG = false;
		SUCCESSFLAG_ENTRY = true;
		successInterval = 120;
		selX = selY = 4;
		//search = false;
		//searchX = searchY = 0;
		
		timer = new SudokuWatch();
		
		count = 60;
		
		countFlag = true;
		timerFlag = true;
		
		prevMouseX = input.getAbsoluteMouseX();
		prevMouseY = input.getAbsoluteMouseY();
		nowMouseX = input.getAbsoluteMouseX();
		nowMouseY = input.getAbsoluteMouseY();

		readyFont2.addGlyphs(readyText);
		readyFont2.getEffects().add(new ColorEffect(java.awt.Color.white));
		readyFont2.loadGlyphs();

		dabunFont.addGlyphs(dabunText);
		dabunFont.getEffects().add(new ColorEffect(java.awt.Color.white));
		dabunFont.loadGlyphs();
		
		questionLevel = 5;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		nowMouseX = input.getAbsoluteMouseX();
		nowMouseY = input.getAbsoluteMouseY();
		
		if(!countFlag && timerFlag) {
			timer.start();
			timerFlag = false;
		}

		if(exitButton.onClick(input)) {
			System.exit(0);
		}
		
		if(input.isKeyDown(Input.KEY_F12) ||
				restartButton.onClick(input) ) {
			this.init(gc);
		}
		
		if(READYFLAG) {
			
			if (selInterval <= 0) {
				if(input.isKeyDown(Input.KEY_UP) && questionLevel < 9) {
					questionLevel++;
					selInterval = 10;
				}
				else if(input.isKeyDown(Input.KEY_DOWN) && questionLevel > 1) {
					questionLevel--;
					selInterval = 10;
				}
			}
			
			if(input.isKeyPressed(Input.KEY_ENTER) || input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				READYFLAG = false;
				Question question = new Question(questionLevel);
				board.setQuestion(question);
			}
			else {
				selInterval--;
			}
		}
		else if(SUCCESSFLAG) {
			if(SUCCESSFLAG_ENTRY) {
				successInterval = 120;
				SUCCESSFLAG_ENTRY = false;
				board.lockAll();
				timer.stopTimer();
			}
			if(successInterval <= 0) {
				if(input.isKeyPressed(Input.KEY_ENTER) || input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					SUCCESSFLAG = false;
					slideX = -60;
				}
			}
			else {
				successInterval--;
			}
		}
		else {
			this.MouseInput(gc);
			this.KeyboardInput(gc);
		}
		
		prevMouseX = nowMouseX;
		prevMouseY = nowMouseY;
	}
	
	public void KeyboardInput(GameContainer gc) {

		if(selInterval <= 0) {
			if(input.isKeyDown(Input.KEY_UP) && selY > 0) {
				selY--;
				selInterval = 10;
			}
			if(input.isKeyDown(Input.KEY_DOWN) && selY < 8) {
				selY++;
				selInterval = 10;
			}
			if(input.isKeyDown(Input.KEY_LEFT) && selX > 0) {
				selX--;
				selInterval = 10;
			}
			if(input.isKeyDown(Input.KEY_RIGHT) && selX < 8) {
				selX++;
				selInterval = 10;
			}
		}
		else {
			selInterval--;
		}


		// input.KEY_1 == 2 (int)
		// input.KEY_9 == 10 (int)

		if(input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {

			for(int i = 2; i <= 10; i++) {
				if(input.isKeyPressed(i))
					board.setSubNumber(selX, selY, i-1);
			}
			for(int i = 0; i < numpad.length; i++) {
				if(input.isKeyPressed(numpad[i]))
					board.setSubNumber(selX, selY, i+1);
			}
			if(input.isKeyPressed(Input.KEY_NUMPAD0) ||
					input.isKeyPressed(Input.KEY_SPACE))	{
				board.clearSub(selX, selY);
			}

		} else {

			for(int i = 2; i <= 10; i++) {
				if(input.isKeyPressed(i)) {
					SUCCESSFLAG = board.setNumber(selX, selY, i-1);
					board.clearSub(selX, selY);
				}
			}
			for(int i = 0; i < numpad.length; i++) {
				if(input.isKeyPressed(numpad[i])) {
					SUCCESSFLAG = board.setNumber(selX, selY, i+1);
					board.clearSub(selX, selY);
				}
			}
			if(input.isKeyPressed(Input.KEY_NUMPAD0) ||
					input.isKeyPressed(Input.KEY_SPACE))	{
				board.setNumber(selX, selY, 0);
				board.clearSub(selX, selY);
			}

		}

		if(input.isKeyPressed(Input.KEY_S)) {
				searchX = selX;
				searchY = selY;
				searchFlag = false;
		}

	}

	public void MouseInput(GameContainer gc) {

		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			if(numBoard.selectNumber()) {
				searchFlag = true;
			}
		}

		mousePoint = board.pointFromMouse(nowMouseX, nowMouseY);
		
		if(!(mousePoint[0] == -1)) {
			if(prevMouseX != nowMouseX || prevMouseY != nowMouseY) {
				selX = mousePoint[0];
				selY = mousePoint[1];
			}

			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				if(numBoard.getSelectNumber() == board.getNumber(selX, selY)) {
					board.setNumber(selX, selY, 0);
				}
				else {
					SUCCESSFLAG =
							board.setNumber(selX, selY, numBoard.getSelectNumber());
					board.clearSub(selX, selY);
				}
			}
			
			if(input.isMousePressed(Input.MOUSE_MIDDLE_BUTTON)) {
				board.setSubNumber(selX, selY, numBoard.getSelectNumber());
			}

			if(input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
				board.setNumber(selX, selY, 0);
				board.clearSub(selX, selY);
			}

		}

	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {

		backgroundImage.draw();
		
		//exitImage.draw(0, 560);
		
		board.drawBoard(g);

		if(!countFlag) {	
			board.fillAssist(selX, selY, g);
			board.fillSelect(selX, selY, g);
			if(searchFlag) {
				board.fillSearch(numBoard.getSelectNumber(), g);
			}
			else {
				board.fillSearch(searchX, searchY, g);
			}
			board.drawNumber(g);
			board.drawSubNumber(g);
		}
		numBoard.draw(g);

		restartButton.draw(g);
//		exitButton.draw(g);
		
		String nowTime = timer.getFormatTime();
		timerFont.drawString(DISPLAY_WIDTH / 2 - timerFont.getWidth(nowTime) / 2 , 10, nowTime, Color.white);
		
		if(READYFLAG) {
			this.drawReady(g);
		}

		if(countFlag && !READYFLAG) {
			this.drawStarting(g);
		}

		if(SUCCESSFLAG) {
			this.drawSuccess(g);
		}
		
		dabunFont.drawString(0, DISPLAY_HEIGHT - dabunFont.getHeight(dabunText),
				dabunText, Color.white);
	}
	
	private void drawReady(Graphics g) {
		
		g.fillRect(0, 200, DISPLAY_WIDTH, 200);
		successFont.drawString(DISPLAY_WIDTH/2 - successFont.getWidth("Slick Sudoku (ver:1.1)")/2,
				DISPLAY_HEIGHT/2 - successFont.getHeight()/2 - 50,
				"Slick Sudoku (ver:1.1)", Color.blue);
		readyFont.drawString(DISPLAY_WIDTH/2 - readyFont.getWidth("Level : " + String.valueOf(questionLevel))/2,
				DISPLAY_HEIGHT/2 - readyFont.getHeight()/2 + 25,
				"Level : " + String.valueOf(questionLevel), Color.blue);
		readyFont2.drawString(DISPLAY_WIDTH/2 - readyFont2.getWidth(readyText)/2,
				DISPLAY_HEIGHT/2 - readyFont2.getHeight("")/2 + 50,
				readyText, Color.blue);
	}
	
	private void drawStarting(Graphics g) {
		
		g.fillRect(0, 200, DISPLAY_WIDTH, 200);
		if(count > 0) {
			successFont.drawString(DISPLAY_WIDTH/2 - successFont.getWidth("Ready...")/2,
									DISPLAY_HEIGHT/2 - successFont.getHeight()/2,
									"Ready...", Color.red);
		}
		else if(count > -30) {
			successFont.drawString(DISPLAY_WIDTH/2 - successFont.getWidth("Go!!")/2,
									DISPLAY_HEIGHT/2 - successFont.getHeight()/2,
									"Go!!", Color.red);
		}
		
		if(count < -30) {
			countFlag = false;
		}
		else {
			count--;
		}
	}

	private void drawSuccess(Graphics g) {
		
		String str = "Complete!!";
		String str2 = "Enter key or Click";
		startSlideX = -successFont.getWidth(str);
		g.fillRect(0, 200, DISPLAY_WIDTH, 200);
		successFont.drawString(startSlideX + slideX, 270, str, Color.red);
		slideX += 3;
		if(startSlideX + slideX > DISPLAY_WIDTH)
			slideX = 0;

		if(strSwitch) {
			enterFont.drawString(DISPLAY_WIDTH/2 - enterFont.getWidth(str2)/2, 350, str2, Color.red);
			if(strInterval < 0) {
				strInterval = 30;
				strSwitch = false;
			}
		} else {
			if(strInterval < 0) {
				strInterval = 30;
				strSwitch = true;
			}
		}
		strInterval--;
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new SlickSudoku());

		app.setDisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, false);
		app.start();
	}


}