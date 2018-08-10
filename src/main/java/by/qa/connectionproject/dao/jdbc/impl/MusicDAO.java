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
import by.qa.connectionproject.dao.IMusicDAO;
import by.qa.connectionproject.models.file.Music;

public class MusicDAO implements IMusicDAO {

	private final static String GET_MUSIC_BY_ID = "SELECT * FROM Music WHERE id=?";
	private final static String GET_ALL_MUSIC = "SELECT * FROM Music";
	private final static String DELETE_MUSIC_BY_ID = "DELETE FROM Music WHERE id=?";
	private final static String INSERT_MUSIC = "INSERT INTO Music (artist_name, song_name, publication_date, profile_id) VALUES (?, ?, ?, ?)";
	private final static String UPDATE_MUSIC = "UPDATE Music SET artist_name = ?, song_name = ?, publication_date = ?, profile_id = ? WHERE (id = ?)";
	private final static String GET_ALL_MUSIC_BY_PROFILE_ID = "SELECT Music.id, Music.artist_name, Music.song_name, Music.publication_date, Music.profile_id FROM Music "
			+ "INNER JOIN Profiles ON Profiles.id=Music.profile_id WHERE Profiles.id IN(?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Music getEntityById(Long id) {
		Music music = new Music();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_MUSIC_BY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setMusicFields(resultSet, music);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return music;
	}

	@Override
	public List<Music> getAll() {
		List<Music> musicCollection = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_MUSIC);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Music music = new Music();
				setMusicFields(resultSet, music);
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

	private Music setMusicFields(ResultSet resultSet, Music music) {
		try {
			music.setId(resultSet.getLong("id"));
			music.setArtistName(resultSet.getString("artist_name"));
			music.setSongName(resultSet.getString("song_name"));
			Timestamp dateTime = resultSet.getTimestamp("publication_date");
			Date date = new Date(dateTime.getTime());
			music.setPublicationDate(date);
			music.setProfileId(resultSet.getLong("profile_id"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return music;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_MUSIC_BY_ID);
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
	public void create(Music music) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_MUSIC);
			statement.setString(1, music.getArtistName());
			statement.setString(2, music.getSongName());
			LocalDateTime localDate = LocalDateTime.now();
			statement.setObject(3, localDate);
			statement.setLong(4, music.getProfileId());
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
	public void update(Music music) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_MUSIC);
			statement.setString(1, music.getArtistName());
			statement.setString(2, music.getSongName());
			statement.setObject(3, music.getPublicationDate());
			statement.setLong(4, music.getProfileId());
			statement.setLong(5, music.getId());
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
	public List<Music> getAllMusicByProfileId(Long id) {
		List<Music> musicCollection = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_MUSIC_BY_PROFILE_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Music music = new Music();
				music.setId(resultSet.getLong("id"));
				music.setArtistName(resultSet.getString("artist_name"));
				music.setSongName(resultSet.getString("song_name"));
				Timestamp dateTime = resultSet.getTimestamp("publication_date");
				Date date = new Date(dateTime.getTime());
				music.setPublicationDate(date);
				music.setProfileId(resultSet.getLong("profile_id"));
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
}
