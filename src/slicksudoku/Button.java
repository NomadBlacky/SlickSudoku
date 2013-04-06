package slicksudoku;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

/**
 * ボタンを扱うクラス
 */
public class Button {

	private float x, y, width, height;
	private String text;
	private TrueTypeFont TTFont;
	private Color lineColor = Color.white;
	private Color textColor = Color.white;

	Button(float x, float y, float width, float height, String text) {

		this.setPosition(x, y);
		this.setSize(width, height);
		this.setText(text);
	}

	Button(float x, float y, float width, float height, String text, TrueTypeFont font) {

		this.setPosition(x, y);
		this.setSize(width, height);
		this.setText(text);
		this.setFont(font);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(float w, float h) {
		width = w;
		height = h;
	}


	/**
	 * ボタン上に表示するテキストを設定
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * フォントを設定(TrueTypeFont)
	 * @param text
	 */
	public void setFont(TrueTypeFont text) {
		this.TTFont = text;
	}

	/**
	 * フォント設定を削除
	 */
	public void clearFont() {
		this.TTFont = null;
	}

	public void setLineColor(Color col) {
		lineColor = col;
	}

	public void setTextColor(Color col) {
		textColor = col;
	}

	/**
	 * ボタンを描画する
	 * @param g
	 */
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
		if(TTFont == null) {
			g.drawString(text,
					x + width/2 - g.getFont().getWidth(text)/2,
					y + height/2 - g.getFont().getHeight(text)/2);
		} else {
			TTFont.drawString(x + width/2 - TTFont.getWidth(text)/2,
					y + height/2 - TTFont.getHeight()/2,
					text);
		}
	}

	/**
	 * 色指定でボタンを描画する
	 * @param g
	 */
	public void draw(Graphics g, Color color) {
		g.setColor(color);
		g.drawRect(x, y, width, height);
		if(TTFont == null) {
			g.drawString(text,
					x + width/2 - g.getFont().getWidth(text)/2,
					y + height/2 - g.getFont().getHeight(text)/2);
		} else {
			TTFont.drawString(x + width/2 - TTFont.getWidth(text)/2,
					y + height/2 - TTFont.getHeight()/2,
					text,
					color);
		}
		g.setColor(Color.white);
	}

	/**
	 * マウスがボタンの上に乗っているかを返す
	 * @param input
	 * @return マウスがボタンの上に乗っている … true
	 */
	public boolean onMouse(Input input) {
		if(x <= input.getAbsoluteMouseX() &&
				input.getAbsoluteMouseX() <= x+width &&
				y <= input.getAbsoluteMouseY() &&
				input.getAbsoluteMouseY() <= y+height) {
			return true;
		}
		return false;
	}

	/**
	 * ボタンの上でクリックしたかを返す
	 * @param input
	 * @return ボタンの上でクリックした … true
	 */
	public boolean onClick(Input input) {
		if(this.onMouse(input) &&
				input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			return true;
		}
		return false;

	}
}
