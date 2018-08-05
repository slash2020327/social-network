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
import by.qa.connectionproject.dao.jdbc.IAbstractDAO;
import by.qa.connectionproject.dao.jdbc.IDialogDAO;
import by.qa.connectionproject.models.Dialog;
import by.qa.connectionproject.models.PrivateMessage;

public class DialogDAO implements IDialogDAO {
	
	private final static String GET_DIALOG_BY_ID = "SELECT * FROM Dialogues WHERE id=?";
	private final static String GET_ALL_DIALOGUES = "SELECT * FROM Dialogues";
	private final static String GET_DIALOG_BY_MESSAGE_ID = "SELECT Dialogues.id, Dialogues.user_id, Dialogues.creation_date FROM Dialogues " + 
			"INNER JOIN Private_messages ON Private_messages.dialog_id=Dialogues.id WHERE Private_messages.id IN(?)";
	private final static String GET_ALL_MESSAGES_BY_DIALOG_ID = "SELECT Private_messages.id, Private_messages.from_user, Private_messages.to_user, Private_messages.text, Private_messages.date_send, Private_messages.dialog_id FROM Private_messages " + 
			"INNER JOIN Dialogues ON Dialogues.id=Private_messages.dialog_id WHERE Dialogues.id IN(?)";
	private final static String DELETE_DIALOG_BY_ID = "DELETE FROM Dialogues WHERE id=?";
	private final static String INSERT_DIALOG = "INSERT INTO Dialogues (user_id, creation_date) VALUES (?)";
	private final static String UPDATE_DIALOG = "UPDATE Dialogues SET user_id = ?, creation_date = ? WHERE (id = ?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Dialog getEntityById(Integer id) {
		Dialog dialog = new Dialog();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_DIALOG_BY_ID);
			statement.setInt(1, id);
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
			dialog.setId(resultSet.getInt("id"));
			dialog.setDialogOwnerId(resultSet.getInt("user_id"));
			Timestamp dateTime = resultSet.getTimestamp("creation_date"); 
			Date date = new Date(dateTime.getTime());
			dialog.setCreationDate(date);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return dialog;
	}
	
	@Override
	public void delete(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_DIALOG_BY_ID);
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Delete from the data base error", e);
		} finally {
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}

	@Override
	public void create(Dialog dialog) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_DIALOG);
			statement.setInt(1, dialog.getDialogOwnerId());
			statement.setObject(2, dialog.getCreationDate());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			logger.log(Level.ERROR, "Creation error", e);
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}
	
	@Override
	public void update(Dialog dialog) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_DIALOG);
			statement.setInt(1, dialog.getDialogOwnerId());
			statement.setObject(2, dialog.getCreationDate());
			statement.setInt(3, dialog.getId());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			logger.log(Level.ERROR, "Update error", e);
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}
	
	@Override
	public List<PrivateMessage> getAllMessagesByDialogId(Integer id) {
		List<PrivateMessage> privateMessages = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_MESSAGES_BY_DIALOG_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				PrivateMessage privateMessage = new PrivateMessage();
				privateMessage.setId(resultSet.getInt("id"));
				privateMessage.setFromUserId(resultSet.getInt("from_user"));
				privateMessage.setToUserId(resultSet.getInt("to_user"));
				privateMessage.setMessageText(resultSet.getString("text"));
				Timestamp dateTime = resultSet.getTimestamp("date_send"); 
				Date date = new Date(dateTime.getTime());
				privateMessage.setDateSend(date);
				privateMessage.setDialogId(resultSet.getInt("dialog_id"));
				privateMessages.add(privateMessage);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return privateMessages;
	}
	
	@Override
	public Dialog getDialogByMessageId(Integer id) {
		Dialog dialog = new Dialog();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_DIALOG_BY_MESSAGE_ID);
			statement.setInt(1, id);
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