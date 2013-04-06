package slicksudoku;

import java.io.File;


public class TestClass {
	
	public void timerTest() {

		SudokuWatch timer = new SudokuWatch();
		
		timer.start();
				
		for(int i = 0; i < 100; i++) {
			System.out.println(timer.getTime());
		}
		
		timer.stopTimer();
	}
	
	public void dataRoaderTest() {
		
		DataRoader dataRoader = new DataRoader();
		File file = dataRoader.getFile("background");
		
		System.out.println(file);
	}

	public static void main(String[] args) {

		TestClass testClass = new TestClass();
		
		testClass.timerTest();
	}

}
