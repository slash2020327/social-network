package by.qa.connectionproject.deadlock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class FirstThread {

	private static Logger logger = LogManager.getLogger();

	synchronized void firstMethod(SecondThread secondThread) {
		String name = Thread.currentThread().getName();
		logger.log(Level.INFO, name + " enter to method firstThread.firstMethod()");
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.log(Level.ERROR, "Class FirstThread was interrupted");
		}
		logger.log(Level.INFO, name + " tries to call method secondThread.last()");
		secondThread.last();
	}

	synchronized void last() {
		logger.log(Level.INFO, "In method firstThread.last()");
	}
}
