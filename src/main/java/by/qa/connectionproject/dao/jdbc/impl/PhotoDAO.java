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
import by.qa.connectionproject.dao.IPhotoDAO;
import by.qa.connectionproject.models.file.Photo;

public class PhotoDAO implements IPhotoDAO {

	private final static String GET_PHOTO_BY_ID = "SELECT * FROM Photos WHERE id=?";
	private final static String GET_ALL_PHOTOS = "SELECT * FROM Photos";
	private final static String DELETE_PHOTO_BY_ID = "DELETE FROM Photos WHERE id=?";
	private final static String INSERT_PHOTO = "INSERT INTO Photos (publication_date, album_id) VALUES (?, ?)";
	private final static String UPDATE_PHOTO = "UPDATE Photos SET publication_date = ?, album_id = ? WHERE (id = ?)";
	private final static String GET_ALL_PHOTOS_BY_ALBUM_ID = "SELECT Photos.id, Photos.publication_date, Photos.album_id FROM Photos "
			+ "INNER JOIN Albums ON Albums.id=Photos.album_id WHERE Albums.id IN(?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Photo getEntityById(Long id) {
		Photo photo = new Photo();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_PHOTO_BY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setPhotoFields(resultSet, photo);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return photo;
	}

	@Override
	public List<Photo> getAll() {
		List<Photo> photos = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_PHOTOS);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Photo photo = new Photo();
				setPhotoFields(resultSet, photo);
				photos.add(photo);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return photos;
	}

	private Photo setPhotoFields(ResultSet resultSet, Photo photo) {
		try {
			photo.setId(resultSet.getLong("id"));
			Timestamp dateTime = resultSet.getTimestamp("publication_date");
			Date date = new Date(dateTime.getTime());
			photo.setPublicationDate(date);
			photo.setAlbumId(resultSet.getLong("album_id"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return photo;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_PHOTO_BY_ID);
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
	public void create(Photo photo) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_PHOTO);
			LocalDateTime localDate = LocalDateTime.now();
			statement.setObject(1, localDate);
			statement.setLong(2, photo.getAlbumId());
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
	public void update(Photo photo) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_PHOTO);
			statement.setObject(1, photo.getPublicationDate());
			statement.setLong(2, photo.getAlbumId());
			statement.setLong(3, photo.getId());
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
	public List<Photo> getAllPhotosByAlbumId(Long id) {
		List<Photo> photoCollection = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_PHOTOS_BY_ALBUM_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Photo photo = new Photo();
				photo.setId(resultSet.getLong("id"));
				Timestamp dateTime = resultSet.getTimestamp("publication_date");
				Date date = new Date(dateTime.getTime());
				photo.setPublicationDate(date);
				photo.setAlbumId(resultSet.getLong("album_id"));
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
}
