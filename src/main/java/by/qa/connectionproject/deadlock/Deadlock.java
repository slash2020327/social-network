package by.qa.connectionproject.deadlock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Deadlock implements Runnable {

	FirstThread firstThread = new FirstThread();
	SecondThread secondThread = new SecondThread();
	private static Logger logger = LogManager.getLogger();

	Deadlock() {
		Thread.currentThread().setName("Main thread");
		Thread t = new Thread(this, "Competitive thread");
		t.start();
		firstThread.firstMethod(secondThread);
		logger.log(Level.INFO, "Back to main thread");
	}

	public void run() {
		secondThread.secondMethod(firstThread);
		logger.log(Level.INFO, "Back to another thread");
	}

	public static void main(String args[]) {
		new Deadlock();
	}
}
