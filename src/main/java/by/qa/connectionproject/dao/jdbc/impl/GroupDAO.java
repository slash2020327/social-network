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
import by.qa.connectionproject.dao.jdbc.IGroupDAO;
import by.qa.connectionproject.models.Group;
import by.qa.connectionproject.models.Profile;

public class GroupDAO implements IGroupDAO {

	private final static String GET_GROUP_BY_ID = "SELECT * FROM Public_groups WHERE id=?";
	private final static String GET_ALL_GROUPS = "SELECT * FROM Public_groups";
	private final static String DELETE_GROUP_BY_ID = "DELETE FROM Public_groups WHERE id=?";
	private final static String INSERT_GROUP = "INSERT INTO Public_groups (name, description) VALUES (?, ?)";
	private final static String UPDATE_GROUP = "UPDATE Public_groups SET name = ?, description = ? WHERE (id = ?)";
	private final static String GET_ALL_PROFILES_BY_GROUP_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles "
			+ "INNER JOIN Groups_has_profiles ON Groups_has_profiles.profile_id=Profiles.id INNER JOIN Public_groups ON Public_groups.id=Groups_has_profiles.group_id WHERE Public_groups.id IN(?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Group getEntityById(Integer id) {
		Group group = new Group();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_GROUP_BY_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setGroupFields(resultSet, group);
			group.setProfiles(getAllProfilesByGroupId(id));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return group;
	}

	@Override
	public List<Group> getAll() {
		List<Group> groups = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_GROUPS);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Group group = new Group();
				setGroupFields(resultSet, group);
				groups.add(group);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return groups;
	}

	private Group setGroupFields(ResultSet resultSet, Group group) {
		try {
			group.setId(resultSet.getInt("id"));
			group.setName(resultSet.getString("name"));
			group.setGroupDescription(resultSet.getString("description"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return group;
	}

	@Override
	public void delete(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_GROUP_BY_ID);
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
	public void create(Group group) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_GROUP);
			statement.setString(1, group.getName());
			statement.setString(2, group.getGroupDescription());
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
	public void update(Group group) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_GROUP);
			statement.setString(1, group.getName());
			statement.setString(2, group.getGroupDescription());
			statement.setInt(3, group.getId());
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
	public List<Profile> getAllProfilesByGroupId(Integer id) {
		List<Profile> profileCollection = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_PROFILES_BY_GROUP_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Profile profile = new Profile();
				profile.setId(resultSet.getInt("id"));
				profile.setStatus(resultSet.getString("status"));
				profile.setLogin(resultSet.getString("login"));
				profile.setPassword(resultSet.getString("password"));
				profileCollection.add(profile);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return profileCollection;
	}
}
