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
import by.qa.connectionproject.dao.jdbc.IVideoDAO;
import by.qa.connectionproject.models.file.Video;

public class VideoDAO implements IVideoDAO {

	private final static String GET_VIDEO_BY_ID = "SELECT * FROM Videos WHERE id=?";
	private final static String GET_ALL_VIDEOS = "SELECT * FROM Videos";
	private final static String DELETE_VIDEO_BY_ID = "DELETE FROM Videos WHERE id=?";
	private final static String INSERT_VIDEO = "INSERT INTO Videos (name, publication_date, profile_id) VALUES (?, ?, ?)";
	private final static String UPDATE_VIDEO = "UPDATE Videos SET name = ?, publication_date = ?, profile_id = ? WHERE (id = ?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Video getEntityById(Integer id) {
		Video video = new Video();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_VIDEO_BY_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setVideoFields(resultSet, video);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return video;
	}

	@Override
	public List<Video> getAll() {
		List<Video> videos = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_VIDEOS);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Video video = new Video();
				setVideoFields(resultSet, video);
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

	private Video setVideoFields(ResultSet resultSet, Video video) {
		try {
			video.setId(resultSet.getInt("id"));
			video.setName(resultSet.getString("name"));
			Timestamp dateTime = resultSet.getTimestamp("publication_date");
			Date date = new Date(dateTime.getTime());
			video.setPublicationDate(date);
			video.setProfileId(resultSet.getInt("profile_id"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return video;
	}

	@Override
	public void delete(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_VIDEO_BY_ID);
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
	public void create(Video video) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_VIDEO);
			statement.setString(1, video.getName());
			statement.setObject(2, video.getPublicationDate());
			statement.setInt(3, video.getProfileId());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			logger.log(Level.ERROR, "Creation error", e);
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}

	@Override
	public void update(Video video) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_VIDEO);
			statement.setString(1, video.getName());
			statement.setObject(2, video.getPublicationDate());
			statement.setInt(3, video.getProfileId());
			statement.setInt(4, video.getId());
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
}
