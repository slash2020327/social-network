package by.qa.connectionproject.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.qa.connectionproject.connection.ConnectionPool;
import by.qa.connectionproject.dao.IAbstractDAO;
import by.qa.connectionproject.dao.IPrivateMessageDAO;
import by.qa.connectionproject.models.PrivateMessage;

public class PrivateMessageDAO implements IPrivateMessageDAO {

	private final static String GET_PRIVATE_MESSAGE_BY_ID = "SELECT * FROM Private_messages WHERE id=?";
	private final static String GET_ALL_PRIVATE_MESSAGES = "SELECT * FROM Private_messages";
	private final static String DELETE_PRIVATE_MESSAGE_BY_ID = "DELETE FROM Private_messages WHERE id=?";
	private final static String INSERT_PRIVATE_MESSAGE = "INSERT INTO Private_messages (from_user, to_user, text, date_send, dialog_id) VALUES (?, ?, ?, ?, ?)";
	private final static String UPDATE_PRIVATE_MESSAGE = "UPDATE Private_messages SET from_user = ?, to_user = ?, text = ?, date_send = ?, dialog_id = ? WHERE (id = ?)";
	private final static String GET_ALL_MESSAGES_BY_DIALOG_ID = "SELECT Private_messages.id, Private_messages.from_user, Private_messages.to_user, Private_messages.text, Private_messages.date_send, Private_messages.dialog_id FROM Private_messages "
			+ "INNER JOIN Dialogues ON Dialogues.id=Private_messages.dialog_id WHERE Dialogues.id IN(?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public PrivateMessage getEntityById(Long id) {
		PrivateMessage privateMessage = new PrivateMessage();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PRIVATE_MESSAGE_BY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setPrivateMessageFields(resultSet, privateMessage);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return privateMessage;
	}

	@Override
	public List<PrivateMessage> getAll() {
		List<PrivateMessage> privateMessages = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_PRIVATE_MESSAGES);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				PrivateMessage privateMessage = new PrivateMessage();
				setPrivateMessageFields(resultSet, privateMessage);
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

	private PrivateMessage setPrivateMessageFields(ResultSet resultSet, PrivateMessage privateMessage) {
		try {
			privateMessage.setId(resultSet.getLong("id"));
			privateMessage.setFromUserId(resultSet.getLong("from_user"));
			privateMessage.setToUserId(resultSet.getLong("to_user"));
			privateMessage.setMessageText(resultSet.getString("text"));
			Timestamp dateTime = resultSet.getTimestamp("date_send");
			Date date = new Date(dateTime.getTime());
			privateMessage.setDateSend(date);
			privateMessage.setDialogId(resultSet.getInt("dialog_id"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return privateMessage;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_PRIVATE_MESSAGE_BY_ID);
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
	public void create(PrivateMessage privateMessage) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_PRIVATE_MESSAGE);
			statement.setLong(1, privateMessage.getFromUserId());
			statement.setLong(2, privateMessage.getToUserId());
			statement.setString(3, privateMessage.getMessageText());
			LocalDateTime localDate = LocalDateTime.now();
			statement.setObject(4, localDate);
			statement.setInt(5, privateMessage.getDialogId());
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
	public void update(PrivateMessage privateMessage) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_PRIVATE_MESSAGE);
			statement.setLong(1, privateMessage.getFromUserId());
			statement.setLong(2, privateMessage.getToUserId());
			statement.setString(3, privateMessage.getMessageText());
			statement.setObject(4, privateMessage.getDateSend());
			statement.setLong(5, privateMessage.getDialogId());
			statement.setLong(6, privateMessage.getId());
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
	public List<PrivateMessage> getAllMessagesByDialogId(Long id) {
		List<PrivateMessage> privateMessages = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_MESSAGES_BY_DIALOG_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				PrivateMessage privateMessage = new PrivateMessage();
				privateMessage.setId(resultSet.getLong("id"));
				privateMessage.setFromUserId(resultSet.getLong("from_user"));
				privateMessage.setToUserId(resultSet.getLong("to_user"));
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
}
