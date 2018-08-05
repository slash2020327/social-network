package by.qa.connectionproject.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface IAbstractDAO<K, E> {

	Logger logger = LogManager.getLogger();

	List<E> getAll();

	E getEntityById(K id);

	void delete(K id);

	// void create(E entity);

	void update(E entity);

	public static void closeResultSet(ResultSet resultSet) {
		try {
			DbUtils.close(resultSet);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "ResultSet closure error", e);
		}
	}

	public static void closePreparedStatement(PreparedStatement statement) {
		try {
			DbUtils.close(statement);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "PreparedStatement closure error", e);
		}
	}

	public static void setTrueAutoCommit(Connection connection) {
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				logger.log(Level.ERROR, "Connection set autocommit error", e);
			}
		} else {
			logger.log(Level.INFO, "Connection is null");
		}
	}
}
