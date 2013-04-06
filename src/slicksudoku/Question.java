package slicksudoku;

import java.util.Random;

/**
 * 問題の生成を扱うクラス
 */
public class Question {

	/**	問題の数字を格納する。ゼロで空白 */
	private int[][] number = new int[9][9];
	/**	難易度（空白の多さ） */
	private int level;

	private Random rnd;

	/**
	 * 難易度を５に設定して、テーブルを初期化する。
	 */
	Question() {
		level = 5;
		init();
		CreateQuestion();
	}

	/**
	 * 渡された難易度を設定して、テーブルを初期化する。
	 * @param level
	 */
	Question(int level) {
		this.level = level;
		init();
		CreateQuestion();
	}

	/**
	 * テーブルを初期化
	 */
	private void init() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				number[j][i] = 0;
			}
		}
	}

	/**
	 * 難易度を設定
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 難易度を取得
	 * @return 難易度
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 力づく法で問題を生成する。（空白は作らない）
	 * @return 成功(true) / 失敗(false)
	 */
	public boolean CreateQuestion() {

		rnd = new Random();

		/** １マスに対する乱数の生成回数 */
		int TRY;
		/** 解が見つからなかった回数 */
		int MISSING = 0;

		int num = 0;
		boolean flg;

		// マスが埋まるまで繰り返す
		for(int y = 0; y < 9; y++) {
			TRY = 0;
			for(int x = 0; x < 9; x++) {

				flg = false;
				// 解が求まるまで繰り返す
				for(;!flg;) {

					// 100回の乱数生成で解が求まらなかったらやり直す
					if(TRY >= 100) {
						MISSING++;
						// 100000回解が求まらなかったら失敗
						if(MISSING >= 100000) {
							System.err.println("問題の生成に失敗");
							return false;
						}
						// 初期化して、table[0][0]に戻る
						TRY = 0;
						init();
						x = y = 0;
					}
					TRY++;

					// 1~9の乱数
					num = rnd.nextInt(9) + 1;

					// 行・列・グループに同じ数字がないか確認
					flg = checkX(x, y, num);
					if(flg)
						flg = checkY(x, y, num);
					if(flg)
						flg = checkSquare(x, y, num);

				}
				// 数字が被らなかったら格納
				number[y][x] = num;
			}
		}

		// 空白を設定
		if(!setHole())
			return false;

		return true;
	}

	/**
	 * 空白（プレイヤーの回答枠）の設定。
	 * @return 成功(True) / 失敗(false)
	 */
	private boolean setHole() {
		rnd = new Random();
		int x, y;
		for(int i = 0; i < 10 + level * 6; i++) {
			x = rnd.nextInt(9);
			y = rnd.nextInt(9);
			if(number[y][x] == 0)
				i--;
			else
				number[y][x] = 0;
		}
		return true;
	}

	/**　
	 * 渡された座標の行の数字をチェックする。
	 * @param x
	 * @param y
	 * @param num
	 * @return 同じ数字が存在しない(true) / 存在する(false)
	　*/
	private boolean checkX(int x, int y , int num) {
		for(int i = 0; i < 9; i++) {
			if(number[y][i] == num)
				return false;
		}
		return true;
	}

	/**　
	 * 渡された座標の列の数字をチェックする。
	 * @param x
	 * @param y
	 * @param num
	 * @return 同じ数字が存在しない(true) / 存在する(false)
	　*/
	private boolean checkY(int x, int y, int num) {
		for(int i = 0; i < 9; i++) {
			if(number[i][x] == num)
				return false;
		}
		return true;
	}

	/**　
	 * 渡された座標の3x3グループの数字をチェックする。
	 * @param x
	 * @param y
	 * @param num
	 * @return 同じ数字が存在しない(true) / 存在する(false)
	　*/
	private boolean checkSquare(int x, int y, int num) {
		int sqx, sqy;

		sqx = x / 3;
		sqx *= 3;
		sqy = y / 3;
		sqy *= 3;

		for(int i = sqy; i < sqy+3; i++) {
			for(int j = sqx; j < sqx+3; j++) {
				if(number[i][j] == num)
					return false;
			}
		}

		return true;
	}

	/**
	 * 渡された座標の数字を返す。
	 * @param x
	 * @param y
	 * @return 座標の数字
	 */
	public int getNumber(int x, int y) {
		return number[y][x];
	}

	/**
	 * Cellクラスに問題の数字を格納する。
	 * ゼロ（空白）以外は数字を固定する。
	 */
	public void QuestionToCell(Cell[][] cell) {

		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 9; x++) {

				cell[y][x].setNumber(number[y][x]);
				cell[y][x].setLock((number[y][x] == 0) ? false : true);

			}
		}
	}
}
