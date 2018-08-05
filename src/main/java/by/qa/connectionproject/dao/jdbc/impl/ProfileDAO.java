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
import by.qa.connectionproject.dao.jdbc.IProfileDAO;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.Group;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;
import by.qa.connectionproject.models.file.Music;
import by.qa.connectionproject.models.file.Video;

public class ProfileDAO implements IProfileDAO {
	
	private final static String GET_PROFILE_BY_ID = "SELECT * FROM Profiles WHERE id=?";
	private final static String GET_ALL_PROFILES = "SELECT * FROM Profiles";
	private final static String DELETE_PROFILE_BY_ID = "DELETE FROM Profiles WHERE id=?";
	private final static String GET_PROFILE_BY_USER_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles " + 
			"INNER JOIN Users ON Users.profile_id=Profiles.id WHERE Users.id IN (?)";
	private final static String GET_PROFILE_BY_MUSIC_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles " + 
			"INNER JOIN Music ON Profiles.id=Music.profile_id WHERE Music.id IN(?)";
	private final static String GET_PROFILE_BY_VIDEO_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles " + 
			"INNER JOIN Videos ON Profiles.id=Videos.profile_id WHERE Videos.id IN(?)";
	private final static String GET_PROFILE_BY_ALBUM_ID = "SELECT Profiles.id, Profiles.status, Profiles.login, Profiles.password FROM Profiles " + 
			"INNER JOIN Albums ON Profiles.id=Albums.profile_id WHERE Albums.id IN(?)";
	private final static String INSERT_USER = "INSERT INTO Users (profile_id, first_name, last_name, phone_number, city_id) VALUES (?, ?, ?, ?, ?)";
	private final static String INSERT_PROFILE = "INSERT INTO Profiles (status, login, password) VALUES (?, ?, ?)";
	private final static String UPDATE_PROFILE = "UPDATE Profiles SET status = ?, login = ?, password = ? WHERE (id = ?)";
	private final static String GET_ALL_MUSIC_BY_PROFILE_ID = "SELECT Music.id, Music.artist_name, Music.song_name, Music.publication_date, Music.profile_id FROM Music " + 
			"INNER JOIN Profiles ON Profiles.id=Music.profile_id WHERE Profiles.id IN(?)";
	private final static String GET_ALL_VIDEO_BY_PROFILE_ID = "SELECT Videos.id, Videos.name, Videos.publication_date, Videos.profile_id FROM Videos " + 
			"INNER JOIN Profiles ON Profiles.id=Videos.profile_id WHERE Profiles.id IN(?)";
	private final static String GET_ALL_ALBUMS_BY_PROFILE_ID = "SELECT Albums.id, Albums.album_name, Albums.profile_id FROM Albums " + 
			"INNER JOIN Profiles ON Profiles.id=Albums.profile_id WHERE Profiles.id IN(?)";
	private final static String GET_ALL_GROUPS_BY_PROFILE_ID = "SELECT Public_groups.id, Public_groups.name, Public_groups.description FROM Public_groups " + 
			"INNER JOIN Groups_has_profiles ON Groups_has_profiles.group_id=Public_groups.id INNER JOIN Profiles ON Profiles.id=Groups_has_profiles.profile_id WHERE Profiles.id IN(?)";
	private final static String PHOTO_NUMBER = "SELECT COUNT(Photos.id) FROM Profiles LEFT JOIN Albums ON Albums.profile_id=Profiles.id "
			+ "LEFT JOIN Photos ON Photos.id=Albums.id WHERE Profiles.id IN(?) GROUP BY Profiles.id";
	private final static String MUSIC_NUMBER = "SELECT COUNT(Music.id) FROM Profiles LEFT JOIN Music ON Music.profile_id=Profiles.id "
			+ "WHERE Profiles.id IN(?) GROUP BY Profiles.id";
	private final static String VIDEO_NUMBER = "SELECT COUNT(Videos.id) FROM Profiles LEFT JOIN Videos ON Videos.profile_id=Profiles.id "
			+ "WHERE Profiles.id IN(?) GROUP BY Profiles.id";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Profile getEntityById(Integer id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_ID);
			statement.setInt(1, id);
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
			profile.setId(resultSet.getInt("id"));
			profile.setStatus(resultSet.getString("status"));
			profile.setLogin(resultSet.getString("login"));
			profile.setPassword(resultSet.getString("password"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return profile;
	}
	
	@Override
	public void delete(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_PROFILE_BY_ID);
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
	
//	@Override
//	public void create(Profile profile) {
//		Connection connection = null;
//		PreparedStatement statement = null;
//		try {
//			connection = ConnectionPool.getInstance().takeConnection();
//			statement = connection.prepareStatement(INSERT_PROFILE);
//			statement.setInt(1, profile.getUser().getId());
//			statement.setString(2, profile.getStatus());
//			statement.setString(3, profile.getLogin());
//			statement.setString(4, profile.getPassword());
//			statement.executeUpdate();
//		} catch (SQLException e) {
//			logger.log(Level.ERROR, "Creation error", e);
//		} finally {
//			IAbstractDAO.closePreparedStatement(statement);
//			ConnectionPool.getInstance().releaseConnection(connection);
//		}
//	}

	@Override
	public void update(Profile profile) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_PROFILE);
			statement.setString(1, profile.getStatus());
			statement.setString(2, profile.getLogin());
			statement.setString(3, profile.getPassword());
			statement.setInt(4, profile.getId());
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
	public List<Music> getAllMusicByProfileId(Integer id) {
		List<Music> musicCollection = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_MUSIC_BY_PROFILE_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Music music = new Music();
				music.setId(resultSet.getInt("id"));
				music.setArtistName(resultSet.getString("artist_name"));
				music.setSongName(resultSet.getString("song_name"));
				Timestamp dateTime = resultSet.getTimestamp("publication_date"); 
				Date date = new Date(dateTime.getTime());
				music.setPublicationDate(date);
				music.setProfileId(resultSet.getInt("profile_id"));
				musicCollection.add(music);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return musicCollection;
	}
	
	@Override
	public List<Video> getAllVideoByProfileId(Integer id) {
		List<Video> videos = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_VIDEO_BY_PROFILE_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Video video = new Video();
				video.setId(resultSet.getInt("id"));
				video.setName(resultSet.getString("name"));
				Timestamp dateTime = resultSet.getTimestamp("publication_date");
				Date date = new Date(dateTime.getTime());
				video.setPublicationDate(date);
				video.setProfileId(resultSet.getInt("profile_id"));
				videos.add(video);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return videos;
	}
	
	@Override
	public List<Album> getAllAlbumsByProfileId(Integer id) {
		List<Album> albums = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_ALBUMS_BY_PROFILE_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Album album = new Album();
				album.setId(resultSet.getInt("id"));
				album.setAlbumName(resultSet.getString("album_name"));
				album.setProfileId(resultSet.getInt("profile_id"));
				albums.add(album);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return albums;
	}
	
	@Override
	public List<Group> getAllGroupsByProfileId(Integer id) {
		List<Group> groups = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_GROUPS_BY_PROFILE_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Group group = new Group();
				group.setId(resultSet.getInt("id"));
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
	
	@Override
	public Profile getProfileByUserId(Integer id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_USER_ID);
			statement.setInt(1, id);
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
	public Profile getProfileByMusicId(Integer id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_MUSIC_ID);
			statement.setInt(1, id);
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
	public Profile getProfileByVideoId(Integer id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_VIDEO_ID);
			statement.setInt(1, id);
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
	public Profile getProfileByAlbumId(Integer id) {
		Profile profile = new Profile();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PROFILE_BY_ALBUM_ID);
			statement.setInt(1, id);
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
	public int getPhotoNumberByProfileId(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int photoNumber = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(PHOTO_NUMBER);
			statement.setInt(1, id);
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

	@Override
	public int getMusicNumberByProfileId(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int musicNumber = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(MUSIC_NUMBER);
			statement.setInt(1, id);
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

	@Override
	public int getVideoNumberByProfileId(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int videoNumber = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(VIDEO_NUMBER);
			statement.setInt(1, id);
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
}
