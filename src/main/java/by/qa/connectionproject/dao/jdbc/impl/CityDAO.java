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
import by.qa.connectionproject.dao.ICityDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Country;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;

public class CityDAO implements ICityDAO {

	private final static String GET_CITY_BY_ID = "SELECT * FROM Cities WHERE id=?";
	private final static String GET_ALL_CITIES = "SELECT * FROM Cities";
	private final static String GET_CITY_BY_USER_ID = "SELECT Cities.id, Cities.city_name, Cities.country_id FROM Cities "
			+ "RIGHT JOIN Users ON Users.city_id=Cities.id WHERE Users.id IN(?)";
	private final static String GET_ALL_USERS_BY_CITY_ID = "SELECT Users.id, Users.profile_id, Users.first_name, Users.last_name, Users.phone_number, Users.city_id FROM Users "
			+ "INNER JOIN Cities ON Cities.id=Users.city_id WHERE Cities.id IN(?)";
	private final static String DELETE_CITY_BY_ID = "DELETE FROM Cities WHERE id=?";
	private final static String INSERT_CITY = "INSERT INTO Cities (city_name, country_id) VALUES (?, ?)";
	private final static String UPDATE_CITY = "UPDATE Cities SET city_name = ?, country_id = ? WHERE (id = ?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public List<City> getAll() {
		List<City> cities = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_CITIES);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				City city = new City();
				setCityFields(resultSet, city);
				cities.add(city);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return cities;
	}

	@Override
	public City getEntityById(Long id) {
		City city = new City();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_CITY_BY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setCityFields(resultSet, city);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return city;
	}

	private City setCityFields(ResultSet resultSet, City city) {
		try {
			Country country = new Country();
			city.setId(resultSet.getLong("id"));
			city.setCityName(resultSet.getString("city_name"));
			country.setId(resultSet.getLong("country_id"));
			city.setCountry(country);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return city;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_CITY_BY_ID);
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
	public void create(City city) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_CITY);
			statement.setString(1, city.getCityName());
			statement.setLong(2, city.getCountry().getId());
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
	public void update(City city) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_CITY);
			statement.setString(1, city.getCityName());
			statement.setLong(2, city.getCountry().getId());
			statement.setLong(3, city.getId());
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
	public List<User> getAllUsersByCityId(Long id) {
		List<User> users = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_USERS_BY_CITY_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				User user = new User();
				Profile profile = new Profile();
				City city = new City();
				user.setId(resultSet.getLong("id"));
				profile.setId(resultSet.getLong("profile_id"));
				user.setProfile(profile);
				user.setFirstName(resultSet.getString("first_name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setPhoneNumber(resultSet.getString("phone_number"));
				city.setId(resultSet.getLong("city_id"));
				user.setCity(city);
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

	@Override
	public City getCityByUserId(Long id) {
		City city = new City();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_CITY_BY_USER_ID);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setCityFields(resultSet, city);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return city;
	}
}
