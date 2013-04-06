package slicksudoku;

public class SudokuWatch extends Thread {
	
	private long time;
	private boolean stop;

	@Override
	public synchronized void start() {
		System.out.println("*** TIMER START ***");
		time = 0;
		stop = false;
		super.start();
	}
	
	@Override
	public void run() {
		
		while(!stop) {
			time++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
		
		System.out.println("*** TIMER STOP ***");
	}
	
	public long getTime() {
		return time;
	}
		
	public String getFormatTime() {
		long timew = time;
		String timeString = "";
		long second = timew / 100;
		
		timeString += String.format("%d", second / 60) + ":";
		timeString += String.format("%1$02d", second % 60) + ":";
		timeString += String.format("%1$02d", timew % 100);
		
		return timeString;
	}
	
	public void stopTimer() {
		stop = true;
	}
		
	public void restartTimer() {
		stop = false;
		time = 0;
	}
	
}
