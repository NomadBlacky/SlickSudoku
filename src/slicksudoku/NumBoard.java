package slicksudoku;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;



public class NumBoard {

	private Button[][] button = new Button[3][3];
	private int selX = 0, selY = 0, selNum = 1;

	private float stx = 575, sty = 180;
	private float width = 150, height = 150;
	private float cellWidth = width/3, cellHeight = height/3;

	private Font font = new Font("Verdana", Font.PLAIN, 36);
	private TrueTypeFont TTFont = new TrueTypeFont(font, true);
	
	private Input input = null;

	NumBoard(Input input) {
		this.input = input;
		this.init();
	}
	
	public void init() {
		
		int n = 1;
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				button[y][x] = new Button(stx + x*cellWidth, sty + y*cellHeight,
											cellHeight-1, cellWidth-1,
											Integer.toString(n++), TTFont);
			}
		}		
	}

	public void draw(Graphics g) {
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				
				if(x == selX && y == selY)
					button[y][x].draw(g, Color.red);
				else
					button[y][x].draw(g);
			}
		}
	}
	
	public boolean selectNumber() {
		
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				
				if(button[y][x].onClick(input)) {
					selX = x;
					selY = y;
					selNum = y*3 + x + 1;
					return true;
				}
				
			}
		}
		return false;
	}
	
	public int getSelectNumber() {
		return selNum;
	}
	
}
