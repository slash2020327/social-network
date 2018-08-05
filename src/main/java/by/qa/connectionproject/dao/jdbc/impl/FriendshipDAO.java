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
import by.qa.connectionproject.dao.jdbc.IAbstractDAO;
import by.qa.connectionproject.dao.jdbc.IFriendshipDAO;
import by.qa.connectionproject.models.Friendship;
import by.qa.connectionproject.models.FriendshipStatus;

public class FriendshipDAO implements IFriendshipDAO {

	private final static String GET_FRIENDSHIP_BY_ID = "SELECT * FROM Friendship WHERE id=?";
	private final static String GET_ALL_FRIENDSHIP = "SELECT * FROM Friendship";
	private final static String DELETE_FRIENDSHIP_BY_ID = "DELETE FROM Friendship WHERE id=?";
	private final static String INSERT_FRIENDSHIP = "INSERT INTO Friendship (user1_id, user2_id, status) VALUES (?, ?, ?)";
	private final static String UPDATE_FRIENDSHIP = "UPDATE Friendship SET user1_id = ?, user2_id = ?, status = ? WHERE (id = ?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Friendship getEntityById(Integer id) {
		Friendship friendship = new Friendship();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_FRIENDSHIP_BY_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setFriendshipFields(resultSet, friendship);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return friendship;
	}

	@Override
	public List<Friendship> getAll() {
		List<Friendship> friendshipCollection = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_FRIENDSHIP);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Friendship friendship = new Friendship();
				setFriendshipFields(resultSet, friendship);
				friendshipCollection.add(friendship);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return friendshipCollection;
	}

	private Friendship setFriendshipFields(ResultSet resultSet, Friendship friendship) {
		try {
			friendship.setId(resultSet.getInt("id"));
			friendship.setUserOneId(resultSet.getInt("user1_id"));
			friendship.setUserTwoId(resultSet.getInt("user2_id"));
			friendship.setStatus(FriendshipStatus.valueOf(resultSet.getString("status")));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return friendship;
	}

	@Override
	public void delete(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_FRIENDSHIP_BY_ID);
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
	public void create(Friendship friendship) {
		Connection connection = null;
		PreparedStatement statementForUserOne = null;
		PreparedStatement statementForUserTwo = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statementForUserOne = connection.prepareStatement(INSERT_FRIENDSHIP);
			statementForUserOne.setInt(1, friendship.getUserOneId());
			statementForUserOne.setInt(2, friendship.getUserTwoId());
			statementForUserOne.setString(3, friendship.getStatus().name());
			statementForUserOne.executeUpdate();
			statementForUserTwo = connection.prepareStatement(INSERT_FRIENDSHIP);
			statementForUserTwo.setInt(1, friendship.getUserTwoId());
			statementForUserTwo.setInt(2, friendship.getUserOneId());
			statementForUserTwo.setString(3, friendship.getStatus().name());
			statementForUserTwo.executeUpdate();
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
			IAbstractDAO.closePreparedStatement(statementForUserOne);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}

	@Override
	public void update(Friendship friendship) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_FRIENDSHIP);
			statement.setInt(1, friendship.getUserOneId());
			statement.setInt(2, friendship.getUserTwoId());
			statement.setString(3, friendship.getStatus().name());
			statement.setInt(4, friendship.getId());
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
}
