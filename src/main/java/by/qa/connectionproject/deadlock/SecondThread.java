package by.qa.connectionproject.deadlock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class SecondThread {

	private static Logger logger = LogManager.getLogger();

	synchronized void secondMethod(FirstThread firstThread) {
		String name = Thread.currentThread().getName();
		logger.log(Level.INFO, name + " enter to method secondThread.secondMethod()");
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.log(Level.ERROR, "Class SecondThread was interrupted", e);
		}
		logger.log(Level.INFO, name + " tries to call method firstThread.last()");
		firstThread.last();
	}

	synchronized void last() {
		logger.log(Level.INFO, "In method secondThread.last()");
	}
}
