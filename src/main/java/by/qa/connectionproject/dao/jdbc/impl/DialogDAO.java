package by.qa.connectionproject.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.qa.connectionproject.connection.ConnectionPool;
import by.qa.connectionproject.dao.IAbstractDAO;
import by.qa.connectionproject.dao.IDialogDAO;
import by.qa.connectionproject.models.Dialog;

public class DialogDAO implements IDialogDAO {

	private final static String GET_DIALOG_BY_ID = "SELECT * FROM Dialogues WHERE id=?";
	private final static String GET_ALL_DIALOGUES = "SELECT * FROM Dialogues";
	private final static String GET_DIALOG_BY_MESSAGE_ID = "SELECT Dialogues.id, Dialogues.user_id, Dialogues.creation_date FROM Dialogues "
			+ "INNER JOIN Private_messages ON Private_messages.dialog_id=Dialogues.id WHERE Private_messages.id IN(?)";
	private final static String DELETE_DIALOG_BY_ID = "DELETE FROM Dialogues WHERE id=?";
	private final static String INSERT_DIALOG = "INSERT INTO Dialogues (user_id, creation_date) VALUES (?)";
	private final static String UPDATE_DIALOG = "UPDATE Dialogues SET user_id = ?, creation_date = ? WHERE (id = ?)";
	private final static String GET_ALL_DIALOGUES_BY_USER_ID = "SELECT Dialogues.id, Dialogues.user_id, Dialogues.creation_date FROM Dialogues "
			+ "INNER JOIN Users ON Users.id=Dialogues.user_id WHERE Users.id IN(?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Dialog getEntityById(Long id) {
		Dialog dialog = new Dialog();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_DIALOG_BY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setDialogFields(resultSet, dialog);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return dialog;
	}

	@Override
	public List<Dialog> getAll() {
		List<Dialog> dialogues = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_DIALOGUES);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Dialog dialog = new Dialog();
				setDialogFields(resultSet, dialog);
				dialogues.add(dialog);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return dialogues;
	}

	private Dialog setDialogFields(ResultSet resultSet, Dialog dialog) {
		try {
			dialog.setId(resultSet.getLong("id"));
			dialog.setDialogOwnerId(resultSet.getLong("user_id"));
			Timestamp dateTime = resultSet.getTimestamp("creation_date");
			Date date = new Date(dateTime.getTime());
			dialog.setCreationDate(date);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return dialog;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_DIALOG_BY_ID);
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Delete from the data base error", e);
		} finally {
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}

	@Override
	public void create(Dialog dialog) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_DIALOG);
			statement.setLong(1, dialog.getDialogOwnerId());
			statement.setObject(2, dialog.getCreationDate());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Creation error", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Connection rollback error", e);
			}
		} finally {
			IAbstractDAO.setTrueAutoCommit(connection);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}

	@Override
	public void update(Dialog dialog) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_DIALOG);
			statement.setLong(1, dialog.getDialogOwnerId());
			statement.setObject(2, dialog.getCreationDate());
			statement.setLong(3, dialog.getId());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Update error", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Connection rollback error", e);
			}
		} finally {
			IAbstractDAO.setTrueAutoCommit(connection);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}

	@Override
	public List<Dialog> getAllDialoguesByUserId(Long id) {
		List<Dialog> dialogues = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_DIALOGUES_BY_USER_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Dialog dialog = new Dialog();
				dialog.setId(resultSet.getLong("id"));
				dialog.setDialogOwnerId(resultSet.getLong("user_id"));
				Timestamp dateTime = resultSet.getTimestamp("creation_date");
				Date date = new Date(dateTime.getTime());
				dialog.setCreationDate(date);
				dialogues.add(dialog);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return dialogues;
	}
	
	@Override
	public Dialog getDialogByMessageId(Long id) {
		Dialog dialog = new Dialog();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_DIALOG_BY_MESSAGE_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setDialogFields(resultSet, dialog);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return dialog;
	}
}