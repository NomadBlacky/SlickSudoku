package slicksudoku;


public class Cell {

	private int number;
	private boolean lock;
	private int[] sub = new int[3];
	private int last = 0;

	Cell() {
		number = 0;
		lock = false;
		for(int i = 0; i < 3; i++)
			sub[i] = 0;
	}

	public void setNumber(int n) {
		if(lock)
			return;
		number = n;
	}

	public int getNumber() {
		return number;
	}

	public void setLock(boolean bool) {
		lock = bool;
	}

	public boolean getLock() {
		return lock;
	}

	public void setSub(int n) {
		if(lock || last >= sub.length)
			return;
		for(int i = 0; i < sub.length; i++) {
			if(sub[i] == n)
				return;
		}
		sub[last++] = n;
	}

	public int[] getSub() {
		return sub;
	}

	public int getSub(int index) {
		return sub[index];
	}

	public void clearSub() {
		for(int i = 0; i < sub.length; i++) {
			sub[i] = 0;
		}
		last = 0;
	}
}