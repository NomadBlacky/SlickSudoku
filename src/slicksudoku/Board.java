package slicksudoku;

import java.awt.Font;

import javax.naming.InitialContext;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;



/**
 * 9x9のテーブルとそのセルの内容を保持し、描画するクラス。
 * @Singleton
 */
public class Board {

	private Cell[][] cells = new Cell[9][9];
	private boolean[] vGroup = new boolean[9];
	private boolean[] hGroup = new boolean[9];
	private boolean[] sGroup = new boolean[9];
	private float stx = 50, sty = 100;
	private float width = 450, height = 450;
	private float cellWidth = width / 9, cellHeight = height / 9;

	private String[] FontStyle = {"Verdana", "Comic Sans MS", "Times New Roman"};
	private Font font = new Font(FontStyle[0], Font.PLAIN, 35);
	private TrueTypeFont TTFont = new TrueTypeFont(font, true);
	private Font fontb = new Font(FontStyle[0], Font.BOLD, 35);
	private TrueTypeFont TTFontBold = new TrueTypeFont(fontb, true);

	public Board() {
		initCell();
		initGroup();
	}
	
	private void initCell() {

		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				cells[i][j] = new Cell();
			}
		}
	}

	private void initGroup() {

		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 9; x++) {
				checkV(x,y);
				checkH(x,y);
				checkS(x,y);
			}
		}
	}
	
	public void setQuestion(Question question) {
		question.QuestionToCell(cells);
		this.initGroup();
	}

	public void drawBoard(Graphics g) {

		float w = width / 9;
		float h = height / 9;
		for(int i = 0; i < 10; i++) {
			g.drawLine(stx, sty + h*i, stx + width, sty + h*i);
			if(i == 10 || i % 3 == 0) {
				g.drawLine(stx - 1, sty + h*i - 1, stx + width - 1, sty + h*i - 1);
				g.drawLine(stx + 1, sty + h*i + 1, stx + width + 1, sty + h*i + 1);
			}
		}
		for(int i = 0; i < 10; i++) {
			g.drawLine(stx + w*i, sty, stx + w*i, sty + height);
			if(i == 10 || i % 3 == 0) {
				g.drawLine(stx + w*i - 1, sty - 1, stx + w*i - 1, sty + height - 1);
				g.drawLine(stx + w*i + 1, sty + 1, stx + w*i + 1, sty + height + 1);
			}
		}
	}

	public void fillAssist(int x, int y, Graphics g) {

		g.setColor(Color.gray);
		for(int i = 0; i < 9; i++) {
			g.fillRect(stx + cellWidth*i+3, sty + y*cellHeight+3, cellWidth-5, cellHeight-5);
		}
		for(int i = 0; i < 9; i++) {
			g.fillRect(stx + x*cellWidth+3, sty + cellHeight*i+3, cellWidth-5, cellHeight-5);
		}

		int sx = 0, sy = 0;
		sx = x / 3;
		sx *= 3;
		sy = y / 3;
		sy *= 3;

		for(int h = sy; h < sy+3; h++) {
			for(int v = sx; v < sx+3; v++) {
				g.fillRect(stx + cellWidth*v + 3, sty + cellHeight*h + 3,
							cellWidth-5, cellHeight-5);
			}
		}

		g.setColor(Color.white);
	}
	
	public void fillSelect(int x, int y, Graphics g) {
		
		g.setColor(Color.red);
		g.fillRect(stx + x*cellWidth+3, sty + y*cellHeight+3, cellWidth-5, cellHeight-5);
		
		g.setColor(Color.white);
	}

	public void fillSearch(int x, int y, Graphics g) {
		if(x < 0 || y < 0)
			return;

		g.setColor(Color.green);
		int highlightNum = cells[y][x].getNumber();

		if(highlightNum == 0) {
			g.setColor(Color.white);
			return;
		}

		for(int h = 0; h < 9; h++) {
			for(int v = 0; v < 9; v++) {

				if(cells[h][v].getNumber() == highlightNum)
					g.fillRect(stx + cellWidth*v + 3, sty + cellHeight*h + 3,
							cellWidth-5, cellHeight-5);
			}
		}

		g.setColor(Color.white);
	}

	public void fillSearch(int num, Graphics g) {
		g.setColor(Color.green);
		int highlightNum = num;

		if(highlightNum == 0) {
			g.setColor(Color.white);
			return;
		}

		for(int h = 0; h < 9; h++) {
			for(int v = 0; v < 9; v++) {

				if(cells[h][v].getNumber() == highlightNum)
					g.fillRect(stx + cellWidth*v + 3, sty + cellHeight*h + 3,
							cellWidth-5, cellHeight-5);
			}
		}

		g.setColor(Color.white);
	}

	public void drawNumber(Graphics g) {

		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 9; x++) {
				int n = cells[y][x].getNumber();
				String s = (n == 0) ? "" : Integer.toString(n);

				if(cells[y][x].getLock())
					TTFontBold.drawString(stx + cellWidth*x + 12, sty + cellHeight*y + 10, s, Color.yellow);
				else
					TTFont.drawString(stx + cellWidth*x + 12, sty + cellHeight*y + 10, s);
			}
		}

	}

	public void drawSubNumber(Graphics g) {

		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 9; x++) {
				int[] n = cells[y][x].getSub();
				for(int i = 0; i < 3; i++) {
					String s = (n[i] == 0) ? "" : Integer.toString(n[i]);
					g.drawString(s, stx + cellWidth*x + i*15 +5, sty + cellHeight*y);
				}
			}
		}
	}

	public boolean setNumber(int x, int y, int num) {

		cells[y][x].setNumber(num);
		if(cells[y][x].getLock())
			return false;
		else
			return answerCheck(x, y);
	}
	
	public int getNumber(int x, int y) {
		return cells[y][x].getNumber();
	}

	public void createQuestion() {
		Question question = new Question();
		question.CreateQuestion();
		question.QuestionToCell(cells);
	}

	public void setSubNumber(int x, int y, int num) {
		cells[y][x].setSub(num);
	}

	public void clearSub(int x, int y) {
		cells[y][x].clearSub();
	}

	public void lockAll() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				cells[i][j].setLock(true);
			}
		}
	}

	private boolean answerCheck(int x, int y) {
		
		if(checkV(x, y) & checkH(x, y) & checkS(x, y))
			return true;
		else
			return false;
	}

	private boolean checkV(int x, int y) {
		boolean[] nine = initBoolArray();
		for(int i = 0; i < 9; i++) {
			int cnum = cells[i][x].getNumber()-1;
			if(cnum == -1) {
				vGroup[x] = false;
				return false;
			}
			if(nine[cnum] == true) {
				vGroup[x] = false;
				return false;
			}
			else
				nine[cnum] = true;
		}
		vGroup[x] = true;

		for(int i = 0; i < 9; i++) {
			if(!vGroup[i])
				return false;
		}

		return true;
	}

	private boolean checkH(int x, int y) {
		boolean[] nine = initBoolArray();
		for(int i = 0; i < 9; i++) {
			int cnum = cells[y][i].getNumber()-1;
			if(cnum == -1) {
				hGroup[y] = false;
				return false;
			}
			if(nine[cnum] == true) {
				hGroup[y] = false;
				return false;
			}
			else
				nine[cnum] = true;
		}
		hGroup[y] = true;

		for(int i = 0; i < 9; i++) {
			if(!hGroup[i])
				return false;
		}

		return true;
	}

	private boolean checkS(int x, int y) {
		boolean[] nine = initBoolArray();
		int sqx = x / 3;
		sqx *= 3;
		int sqy = y / 3;
		sqy *= 3;
		int index = sqy + sqx/3;

		for(int i = sqy; i < sqy+3; i++) {
			for(int j = sqx; j < sqx+3; j++) {
				int cnum = cells[i][j].getNumber()-1;
				if(cnum == -1) {
					sGroup[index] = false;
					return false;
				}
				if(nine[cnum] == true) {
					sGroup[index] = false;
					return false;
				}
				else
					nine[cnum] = true;
			}
		}
		sGroup[index] = true;

		for(int i = 0; i < 9; i++) {
			if(!sGroup[i])
				return false;
		}

		return true;
	}

	private boolean[] initBoolArray() {
		boolean array[] = new boolean[9];
		for(int i = 0; i < 9; i++) {
			array[i] = false;
		}
		return array;
	}

	public int[] pointFromMouse(int mouseX, int mouseY) {
		int[] address = new int[2];

		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 9; x++) {
				if(stx+x*cellWidth <= mouseX &&
						mouseX <= stx+x*cellWidth+cellWidth-1 &&
						sty+y*cellHeight <= mouseY &&
						mouseY <= sty+y*cellHeight+cellHeight-1) {
					address[0] = x;
					address[1] = y;
					return address;
				}
			}
		}

		address[0] = -1;
		address[1] = -1;
		return address;
	}

}