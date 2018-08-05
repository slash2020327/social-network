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
import by.qa.connectionproject.dao.jdbc.IAlbumDAO;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.file.Photo;

public class AlbumDAO implements IAlbumDAO {

	private final static String GET_ALBUM_BY_ID = "SELECT * FROM Albums WHERE id=?";
	private final static String GET_ALL_ALBUMS = "SELECT * FROM Albums";
	private final static String GET_ALBUM_BY_PHOTO_ID = "SELECT Albums.id, Albums.album_name, Albums.profile_id FROM Albums "
			+ "INNER JOIN Photos ON Photos.album_id=Albums.id WHERE Photos.id IN(?)";
	private final static String DELETE_ALBUM_BY_ID = "DELETE FROM Albums WHERE id=?";
	private final static String INSERT_ALBUM = "INSERT INTO Albums (album_name, profile_id) VALUES (?, ?)";
	private final static String UPDATE_ALBUM = "UPDATE Albums SET album_name = ?, profile_id = ? WHERE (id = ?)";
	private final static String GET_ALL_PHOTOS_BY_ALBUM_ID = "SELECT Photos.id, Photos.publication_date, Photos.album_id FROM Photos "
			+ "INNER JOIN Albums ON Albums.id=Photos.album_id WHERE Albums.id IN(?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Album getEntityById(Integer id) {
		Album album = new Album();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALBUM_BY_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setAlbumFields(resultSet, album);
			album.setPhotos(getAllPhotosByAlbumId(id));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return album;
	}

	@Override
	public List<Album> getAll() {
		List<Album> albums = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_ALBUMS);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Album album = new Album();
				setAlbumFields(resultSet, album);
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

	private Album setAlbumFields(ResultSet resultSet, Album album) {
		try {
			album.setId(resultSet.getInt("id"));
			album.setAlbumName(resultSet.getString("album_name"));
			album.setProfileId(resultSet.getInt("profile_id"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return album;
	}

	@Override
	public void delete(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_ALBUM_BY_ID);
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
	public void create(Album album) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_ALBUM);
			statement.setString(1, album.getAlbumName());
			statement.setInt(2, album.getProfileId());
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
	public List<Photo> getAllPhotosByAlbumId(Integer id) {
		List<Photo> photoCollection = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_PHOTOS_BY_ALBUM_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Photo photo = new Photo();
				photo.setId(resultSet.getInt("id"));
				Timestamp dateTime = resultSet.getTimestamp("publication_date");
				Date date = new Date(dateTime.getTime());
				photo.setPublicationDate(date);
				photo.setAlbumId(resultSet.getInt("album_id"));
				photoCollection.add(photo);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return photoCollection;
	}

	@Override
	public void update(Album album) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_ALBUM);
			statement.setString(1, album.getAlbumName());
			statement.setInt(2, album.getProfileId());
			statement.setInt(3, album.getId());
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
	public Album getAlbumByPhotoId(Integer id) {
		Album album = new Album();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALBUM_BY_PHOTO_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setAlbumFields(resultSet, album);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return album;
	}
}
