package by.qa.connectionproject.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.dbutils.DbUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConnectionPool {

	private String url;
	private String username;
	private String password;
	private int poolSize;
	private BlockingQueue<Connection> connections;
	private static final ConnectionPool connectionPool = new ConnectionPool();
	private ReentrantLock locker = new ReentrantLock();
	private static Logger logger = LogManager.getLogger();
	public final static String DEFAULT_PATH_TO_PROPERTIES = "src/main/resources/database.properties";

	private ConnectionPool() {
	}

	public static ConnectionPool getInstance() {
		return connectionPool;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public void init() {
		FileInputStream fis;
		Properties property = new Properties();
		try {
			fis = new FileInputStream(DEFAULT_PATH_TO_PROPERTIES);
			property.load(fis);
			connectionPool.setUrl(property.getProperty("db.url"));
			connectionPool.setUsername(property.getProperty("db.login"));
			connectionPool.setPassword(property.getProperty("db.password"));
			connectionPool.setPoolSize(Integer.parseInt(property.getProperty("db.poolSize")));
			Class.forName(property.getProperty("db.driver"));
			connections = new ArrayBlockingQueue<Connection>(getPoolSize());
			Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
			connections.add(connection);
			logger.log(Level.INFO, "The new connection was created");
			poolSize--;
			logger.log(Level.INFO, "The connection pool was initialized");
		} catch (SQLException e) {
			logger.log(Level.FATAL, "The connection pool initialization error", e);
		} catch (IOException e) {
			logger.log(Level.ERROR, "Properties file for database not found", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.ERROR, "Connection driver class not found", e);
		}
	}

	public Connection takeConnection() throws SQLException {
		locker.lock();
		Connection connection = null;
		Connection newConnection = null;
		try {
			if (connections.size() == 0 && poolSize > 0) {
				poolSize--;
				logger.log(Level.INFO, poolSize);
				newConnection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
				logger.log(Level.INFO, "The new connection was created");
				connections.add(newConnection);
				connection = connections.take();
				logger.log(Level.INFO, "The connection was taken");
			} else {
				connection = connections.take();
				logger.log(Level.INFO, "The connection was taken");
			}
		} catch (InterruptedException e) {
			logger.log(Level.ERROR, "The connection error", e);
		} finally {
			locker.unlock();
		}
		return connection;
	}

	public void releaseConnection(Connection connection) {
		if (connection != null) {
			connections.add(connection);
			logger.log(Level.INFO, "The connection was released");
		} else {
			closeConnection(connection);
			logger.log(Level.INFO, "The connection is null");
		}
	}

	public void closeConnection(Connection connection) {
		DbUtils.closeQuietly(connection);
		poolSize--;
		logger.log(Level.INFO, "The connection was closed");
	}
}