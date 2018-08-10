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
import by.qa.connectionproject.dao.IGroupDAO;
import by.qa.connectionproject.models.Group;

public class GroupDAO implements IGroupDAO {

	private final static String GET_GROUP_BY_ID = "SELECT * FROM Public_groups WHERE id=?";
	private final static String GET_ALL_GROUPS = "SELECT * FROM Public_groups";
	private final static String DELETE_GROUP_BY_ID = "DELETE FROM Public_groups WHERE id=?";
	private final static String INSERT_GROUP = "INSERT INTO Public_groups (name, description) VALUES (?, ?)";
	private final static String UPDATE_GROUP = "UPDATE Public_groups SET name = ?, description = ? WHERE (id = ?)";
	private final static String GET_ALL_GROUPS_BY_PROFILE_ID = "SELECT Public_groups.id, Public_groups.name, Public_groups.description FROM Public_groups "
			+ "INNER JOIN Groups_has_profiles ON Groups_has_profiles.group_id=Public_groups.id INNER JOIN Profiles ON Profiles.id=Groups_has_profiles.profile_id WHERE Profiles.id IN(?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Group getEntityById(Long id) {
		Group group = new Group();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_GROUP_BY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setGroupFields(resultSet, group);
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
			group.setId(resultSet.getLong("id"));
			group.setName(resultSet.getString("name"));
			group.setGroupDescription(resultSet.getString("description"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return group;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_GROUP_BY_ID);
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
			statement.setLong(3, group.getId());
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
	public List<Group> getAllGroupsByProfileId(Long id) {
		List<Group> groups = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_GROUPS_BY_PROFILE_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Group group = new Group();
				group.setId(resultSet.getLong("id"));
				group.setName(resultSet.getString("name"));
				group.setGroupDescription(resultSet.getString("description"));
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
}
