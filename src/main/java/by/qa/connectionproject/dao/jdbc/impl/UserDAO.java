package by.qa.connectionproject.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.qa.connectionproject.connection.ConnectionPool;
import by.qa.connectionproject.dao.IAbstractDAO;
import by.qa.connectionproject.dao.IUserDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;

public class UserDAO implements IUserDAO {

	private final static String GET_USER_BY_ID = "SELECT * FROM Users WHERE id=?";
	private final static String GET_ALL_USERS = "SELECT * FROM Users";
	private final static String DELETE_USER_BY_ID = "DELETE FROM Users WHERE id=?";
	private final static String GET_USER_BY_DIALOG_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Dialogues ON Dialogues.user_id=Users.id WHERE Dialogues.id IN(?)";
	private final static String GET_FROM_USER_BY_MESSAGE_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Private_messages ON Private_messages.from_user=Users.id WHERE Private_messages.id IN(?)";
	private final static String GET_TO_USER_BY_MESSAGE_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Private_messages ON Private_messages.to_user=Users.id WHERE Private_messages.id IN(?)";
	private final static String GET_USER_ONE_BY_FRIENDSHIP_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Friendship ON Friendship.user1_id=Users.id WHERE Friendship.id IN(?)";
	private final static String GET_USER_TWO_BY_FRIENDSHIP_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Friendship ON Friendship.user2_id=Users.id WHERE Friendship.id IN(?)";
	private final static String GET_ALL_USERS_BY_CITY_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Cities ON Cities.id=Users.city_id WHERE Cities.id IN(?)";
	private final static String INSERT_USER = "INSERT INTO Users (profile_id, first_name, last_name, phone_number, city_id) VALUES (?, ?, ?, ?, ?)";
	private final static String UPDATE_USER = "UPDATE Users SET profile_id = ?, first_name = ?, last_name = ?, phone_number = ?, city_id = ? WHERE (id = ?)";
	private static Logger logger = LogManager.getLogger();

	public static String getInsertUser() {
		return INSERT_USER;
	}

	@Override
	public User getEntityById(Long id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_BY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setUserFields(resultSet, user);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return user;
	}

	@Override
	public List<User> getAll() {
		List<User> users = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_USERS);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				User user = new User();
				setUserFields(resultSet, user);
				users.add(user);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return users;
	}

	private User setUserFields(ResultSet resultSet, User user) {
		try {
			Profile profile = new Profile();
			City city = new City();
			user.setId(resultSet.getLong("id"));
			profile.setId(resultSet.getLong("profile_id"));
			user.setProfile(profile);
			user.setFirstName(resultSet.getString("first_name"));
			user.setLastName(resultSet.getString("last_name"));
			user.setPhoneNumber(resultSet.getString("phone_number"));
			city.setId(resultSet.getLong("city_id"));
			user.setCity(city);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return user;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_USER_BY_ID);
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Delete from the data base error", e);
		} finally {
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}

	// @Override
	// public void create(User user) {
	// Connection connection = null;
	// PreparedStatement statement = null;
	// try {
	// connection = ConnectionPool.getInstance().takeConnection();
	// statement = connection.prepareStatement(INSERT_USER);
	// statement.setString(1, user.getFirstName());
	// statement.setString(2, user.getLastName());
	// statement.setInt(3, user.getPhoneNumber());
	// statement.setInt(4, user.getCity().getId());
	// statement.executeUpdate();
	// } catch (SQLException e) {
	// logger.log(Level.ERROR, "Creation error", e);
	// } finally {
	// IAbstractDAO.closePreparedStatement(statement);
	// ConnectionPool.getInstance().releaseConnection(connection);
	// }
	// }

	@Override
	public void update(User user) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_USER);
			statement.setLong(1, user.getProfile().getId());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setString(4, user.getPhoneNumber());
			statement.setLong(5, user.getCity().getId());
			statement.setLong(6, user.getId());
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
	public List<User> getAllUsersByCityId(Long id) {
		List<User> users = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_USERS_BY_CITY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				User user = new User();
				Profile profile = new Profile();
				City city = new City();
				user.setId(resultSet.getLong("id"));
				profile.setId(resultSet.getLong("profile_id"));
				user.setProfile(profile);
				user.setFirstName(resultSet.getString("first_name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setPhoneNumber(resultSet.getString("phone_number"));
				city.setId(resultSet.getLong("city_id"));
				user.setCity(city);
				users.add(user);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return users;
	}
	
	@Override
	public User getUserByDialogId(Long id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_BY_DIALOG_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setUserFields(resultSet, user);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return user;
	}

	@Override
	public User getFromUserByPrivateMessageId(Long id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_FROM_USER_BY_MESSAGE_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setUserFields(resultSet, user);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return user;
	}

	@Override
	public User getToUserByPrivateMessageId(Long id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_TO_USER_BY_MESSAGE_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setUserFields(resultSet, user);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return user;
	}

	@Override
	public User getUserOneByFriendshipId(Long id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_ONE_BY_FRIENDSHIP_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setUserFields(resultSet, user);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return user;
	}

	@Override
	public User getUserTwoByFriendshipId(Long id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_TWO_BY_FRIENDSHIP_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setUserFields(resultSet, user);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return user;
	}
}
