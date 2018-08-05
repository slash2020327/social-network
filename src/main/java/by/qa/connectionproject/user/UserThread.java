package by.qa.connectionproject.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.qa.connectionproject.connection.ConnectionPool;

public class UserThread extends Thread {

	private static Logger logger = LogManager.getLogger();
	
	@Override
	public void run() {
		Connection connection = null;
		Random random = new Random();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			Thread.sleep(1000 + random.nextInt(2000));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Thread error", e);
		} catch (InterruptedException e) {
			logger.log(Level.ERROR, "Thread error", e);
		} finally {
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}
}
