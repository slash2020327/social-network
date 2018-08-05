package by.qa.connectionproject.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.dbutils.DbUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.qa.connectionproject.connection.ConnectionPool;
import by.qa.connectionproject.dao.jdbc.IAbstractDAO;
import by.qa.connectionproject.dao.jdbc.IUserDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Dialog;
import by.qa.connectionproject.models.Friendship;
import by.qa.connectionproject.models.FriendshipStatus;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;

public class UserDAO implements IUserDAO {

	private final static String GET_USER_BY_ID = "SELECT * FROM Users WHERE id=?";
	private final static String GET_ALL_USERS = "SELECT * FROM Users";
	private final static String DELETE_USER_BY_ID = "DELETE FROM Users WHERE id=?";
	private final static String GET_ALL_DIALOGUES_BY_USER_ID = "SELECT Dialogues.id, Dialogues.user_id, Dialogues.creation_date FROM Dialogues " + 
			"INNER JOIN Users ON Users.id=Dialogues.user_id WHERE Users.id IN(?)";
	private final static String GET_USER_BY_DIALOG_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users " + 
			"INNER JOIN Dialogues ON Dialogues.user_id=Users.id WHERE Dialogues.id IN(?)";
	private final static String GET_FROM_USER_BY_MESSAGE_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Private_messages ON Private_messages.from_user=Users.id WHERE Private_messages.id IN(?)";
	private final static String GET_TO_USER_BY_MESSAGE_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Private_messages ON Private_messages.to_user=Users.id WHERE Private_messages.id IN(?)";
	private final static String GET_USER_ONE_BY_FRIENDSHIP_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Friendship ON Friendship.user1_id=Users.id WHERE Friendship.id IN(?)";
	private final static String GET_USER_TWO_BY_FRIENDSHIP_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Friendship ON Friendship.user2_id=Users.id WHERE Friendship.id IN(?)";
	private final static String GET_ALL_FRIENDSHIP_BY_USER_ID = "SELECT Friendship.id, Friendship.user1_id, Friendship.user2_id, Friendship.status FROM Friendship "
			+ "INNER JOIN Users ON Users.id=Friendship.user1_id WHERE Users.id IN(?)";
	private final static String INSERT_USER = "INSERT INTO Users (profile_id, first_name, last_name, phone_number, city_id) VALUES (?, ?, ?, ?, ?)";
	private final static String INSERT_PROFILE = "INSERT INTO Profiles (status, login, password) VALUES (?, ?, ?)";
	private final static String UPDATE_USER = "UPDATE Users SET profile_id = ?, first_name = ?, last_name = ?, phone_number = ?, city_id = ? WHERE (id = ?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public User getEntityById(Integer id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_BY_ID);
			statement.setInt(1, id);
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
			user.setId(resultSet.getInt("id"));
			profile.setId(resultSet.getInt("profile_id"));
			user.setProfile(profile);
			user.setFirstName(resultSet.getString("first_name"));
			user.setLastName(resultSet.getString("last_name"));
			user.setPhoneNumber(resultSet.getString("phone_number"));
			city.setId(resultSet.getInt("city_id"));
			user.setCity(city);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return user;
	}

	@Override
	public void delete(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_USER_BY_ID);
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
	public void create(User user, Profile profile) throws SQLException {
		Connection connection = null;
		PreparedStatement statementUser = null;
		PreparedStatement statementProfile = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statementProfile = connection.prepareStatement(INSERT_PROFILE, Statement.RETURN_GENERATED_KEYS);
			statementProfile.setString(1, profile.getStatus());
			statementProfile.setString(2, profile.getLogin());
			statementProfile.setString(3, profile.getPassword());
			statementProfile.executeUpdate();
			statementUser = connection.prepareStatement(INSERT_USER);
			statementUser.setString(2, user.getFirstName());
			statementUser.setString(3, user.getLastName());
			statementUser.setString(4, user.getPhoneNumber());
			statementUser.setInt(5, user.getCity().getId());
			resultSet = statementProfile.getGeneratedKeys();
			if (resultSet.next()) {
				statementUser.setInt(1, resultSet.getInt(1));
			}
			statementUser.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Creation error", e);
			connection.rollback();
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			DbUtils.close(resultSet);
			DbUtils.close(statementProfile);
			DbUtils.close(statementUser);
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
	public void update(User user) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_USER);
			statement.setInt(1, user.getProfile().getId());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setString(4, user.getPhoneNumber());
			statement.setInt(5, user.getCity().getId());
			statement.setInt(6, user.getId());
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
	public List<Dialog> getAllDialoguesByUserId(Integer id) {
		List<Dialog> dialogues = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_DIALOGUES_BY_USER_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Dialog dialog = new Dialog();
				dialog.setId(resultSet.getInt("id"));
				dialog.setDialogOwnerId(resultSet.getInt("user_id"));
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
	public List<Friendship> getAllFriendshipByUserId(Integer id) {
		List<Friendship> friendshipList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_FRIENDSHIP_BY_USER_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Friendship friendship = new Friendship();
				friendship.setId(resultSet.getInt("id"));
				friendship.setUserOneId(resultSet.getInt("user1_id"));
				friendship.setUserTwoId(resultSet.getInt("user2_id"));
				friendship.setStatus(FriendshipStatus.valueOf(resultSet.getString("status")));
				friendshipList.add(friendship);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return friendshipList;
	}

	@Override
	public User getUserByDialogId(Integer id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_BY_DIALOG_ID);
			statement.setInt(1, id);
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
	public User getFromUserByPrivateMessage(Integer id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_FROM_USER_BY_MESSAGE_ID);
			statement.setInt(1, id);
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
	public User getToUserByPrivateMessage(Integer id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_TO_USER_BY_MESSAGE_ID);
			statement.setInt(1, id);
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
	public User getUserOneByFriendshipId(Integer id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_ONE_BY_FRIENDSHIP_ID);
			statement.setInt(1, id);
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
	public User getUserTwoByFriendshipId(Integer id) {
		User user = new User();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_TWO_BY_FRIENDSHIP_ID);
			statement.setInt(1, id);
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
