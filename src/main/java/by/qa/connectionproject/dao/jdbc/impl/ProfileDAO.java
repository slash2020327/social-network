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
import by.qa.connectionproject.dao.IProfileDAO;
import by.qa.connectionproject.models.Profile;

public class ProfileDAO implements IProfileDAO {

	private final static String GET_PROFILE_BY_ID = "SELECT * FROM Profiles WHERE id=?";
	private final static String GET_ALL_PROFILES = "SELECT * FROM Profiles";
	private final static String DELETE_PROFILE_BY_ID = "DELETE FROM Profiles WHERE id=?";
	private final static String GET_PROFILE_BY_USER_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles "
			+ "INNER JOIN Users ON Users.profile_id=Profiles.id WHERE Users.id IN (?)";
	private final static String GET_PROFILE_BY_MUSIC_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles "
			+ "INNER JOIN Music ON Profiles.id=Music.profile_id WHERE Music.id IN(?)";
	private final static String GET_PROFILE_BY_VIDEO_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles "
			+ "INNER JOIN Videos ON Profiles.id=Videos.profile_id WHERE Videos.id IN(?)";
	private final static String GET_PROFILE_BY_ALBUM_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles "
			+ "INNER JOIN Albums ON Profiles.id=Albums.profile_id WHERE Albums.id IN(?)";
	private final static String GET_ALL_PROFILES_BY_GROUP_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles "
			+ "INNER JOIN Groups_has_profiles ON Groups_has_profiles.profile_id=Profiles.id INNER JOIN Public_groups ON Public_groups.id=Groups_has_profiles.group_id WHERE Public_groups.id IN(?)";
	private final static String INSERT_PROFILE = "INSERT INTO Profiles (status, login, password) VALUES (?, ?, ?)";
	private final static String UPDATE_PROFILE = "UPDATE Profiles SET status = ?, login = ?, password = ? WHERE (id = ?)";
	private final static String PHOTO_NUMBER = "SELECT COUNT(Photos.id) FROM Profiles LEFT JOIN Albums ON Albums.profile_id=Profiles.id "
			+ "LEFT JOIN Photos ON Photos.id=Albums.id WHERE Profiles.id IN(?) GROUP BY Profiles.id";
	private final static String MUSIC_NUMBER = "SELECT COUNT(Music.id) FROM Profiles LEFT JOIN Music ON Music.profile_id=Profiles.id "
			+ "WHERE Profiles.id IN(?) GROUP BY Profiles.id";
	private final static String VIDEO_NUMBER = "SELECT COUNT(Videos.id) FROM Profiles LEFT JOIN Videos ON Videos.profile_id=Profiles.id "
			+ "WHERE Profiles.id IN(?) GROUP BY Profiles.id";
	private static Logger logger = LogManager.getLogger();

	public static String getInsertProfile() {
		return INSERT_PROFILE;
	}

	@Override
	public Profile getEntityById(Long id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setProfileFields(resultSet, profile);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return profile;
	}

	@Override
	public List<Profile> getAll() {
		List<Profile> profiles = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_PROFILES);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Profile profile = new Profile();
				setProfileFields(resultSet, profile);
				profiles.add(profile);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return profiles;
	}

	private Profile setProfileFields(ResultSet resultSet, Profile profile) {
		try {
			profile.setId(resultSet.getLong("id"));
			profile.setStatus(resultSet.getString("status"));
			profile.setLogin(resultSet.getString("login"));
			profile.setPassword(resultSet.getString("password"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return profile;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_PROFILE_BY_ID);
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
	// public void create(Profile profile) {
	// Connection connection = null;
	// PreparedStatement statement = null;
	// try {
	// connection = ConnectionPool.getInstance().takeConnection();
	// statement = connection.prepareStatement(INSERT_PROFILE);
	// statement.setInt(1, profile.getUser().getId());
	// statement.setString(2, profile.getStatus());
	// statement.setString(3, profile.getLogin());
	// statement.setString(4, profile.getPassword());
	// statement.executeUpdate();
	// } catch (SQLException e) {
	// logger.log(Level.ERROR, "Creation error", e);
	// } finally {
	// IAbstractDAO.closePreparedStatement(statement);
	// ConnectionPool.getInstance().releaseConnection(connection);
	// }
	// }

	@Override
	public void update(Profile profile) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_PROFILE);
			statement.setString(1, profile.getStatus());
			statement.setString(2, profile.getLogin());
			statement.setString(3, profile.getPassword());
			statement.setLong(4, profile.getId());
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
	public Profile getProfileByUserId(Long id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_USER_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setProfileFields(resultSet, profile);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return profile;
	}

	@Override
	public Profile getProfileByMusicId(Long id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_MUSIC_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setProfileFields(resultSet, profile);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return profile;
	}

	@Override
	public Profile getProfileByVideoId(Long id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_VIDEO_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setProfileFields(resultSet, profile);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return profile;
	}

	@Override
	public Profile getProfileByAlbumId(Long id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_ALBUM_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setProfileFields(resultSet, profile);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return profile;
	}

	public int getPhotoNumberByProfileId(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int photoNumber = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(PHOTO_NUMBER);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			photoNumber = resultSet.getInt("COUNT(Photos.id)");
			logger.log(Level.INFO, "Number of photo " + photoNumber);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return photoNumber;
	}

	public int getMusicNumberByProfileId(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int musicNumber = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(MUSIC_NUMBER);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			musicNumber = resultSet.getInt("COUNT(Music.id)");
			logger.log(Level.INFO, "Number of music " + musicNumber);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return musicNumber;
	}

	public int getVideoNumberByProfileId(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int videoNumber = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(VIDEO_NUMBER);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			videoNumber = resultSet.getInt("COUNT(Videos.id)");
			logger.log(Level.INFO, "Number of video " + videoNumber);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return videoNumber;
	}
	
	@Override
	public List<Profile> getAllProfilesByGroupId(Long id) {
		List<Profile> profileCollection = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_PROFILES_BY_GROUP_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Profile profile = new Profile();
				profile.setId(resultSet.getLong("id"));
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
