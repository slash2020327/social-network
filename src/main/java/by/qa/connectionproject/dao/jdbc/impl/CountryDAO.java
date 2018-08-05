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
import by.qa.connectionproject.dao.jdbc.ICountryDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Country;

public class CountryDAO implements ICountryDAO {

	private final static String GET_COUNTRY_BY_ID = "SELECT * FROM Countries WHERE id=?";
	private final static String GET_ALL_COUNTRIES = "SELECT * FROM Countries";
	private final static String GET_COUNTRY_BY_CITY_ID = "SELECT Countries.id, Countries.country_name FROM Countries "
			+ "INNER JOIN Cities ON Cities.country_id=Countries.id WHERE Cities.id IN(?)";
	private final static String GET_ALL_CITIES_BY_COUNTRY_ID = "SELECT Cities.id, Cities.city_name, Cities.country_id FROM Cities "
			+ "INNER JOIN Countries ON Countries.id=Cities.country_id WHERE Countries.id IN(?)";
	private final static String DELETE_COUNTRY_BY_ID = "DELETE FROM Countries WHERE id=?";
	private final static String INSERT_COUNTRY = "INSERT INTO Countries (country_name) VALUES (?)";
	private final static String UPDATE_COUNTRY = "UPDATE Countries SET country_name = ? WHERE (id = ?)";
	private static Logger logger = LogManager.getLogger();

	@Override
	public Country getEntityById(Integer id) {
		Country country = new Country();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_COUNTRY_BY_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setCountryFields(resultSet, country);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return country;
	}

	@Override
	public List<Country> getAll() {
		List<Country> countries = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_COUNTRIES);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Country country = new Country();
				setCountryFields(resultSet, country);
				countries.add(country);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return countries;
	}

	private Country setCountryFields(ResultSet resultSet, Country country) {
		try {
			country.setId(resultSet.getInt("id"));
			country.setCountryName(resultSet.getString("country_name"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		}
		return country;
	}

	@Override
	public void delete(Integer id) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_COUNTRY_BY_ID);
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
	public void create(Country country) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_COUNTRY);
			statement.setString(1, country.getCountryName());
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
	public void update(Country country) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(UPDATE_COUNTRY);
			statement.setString(1, country.getCountryName());
			statement.setInt(2, country.getId());
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
	public List<City> getAllCitiesByCountryId(Integer id) {
		List<City> cities = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_ALL_CITIES_BY_COUNTRY_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Country country = new Country();
				City city = new City();
				city.setId(resultSet.getInt("id"));
				city.setCityName(resultSet.getString("city_name"));
				country.setId(resultSet.getInt("country_id"));
				city.setCountry(country);
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
	public Country getCountryByCityId(Integer id) {
		Country country = new Country();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_COUNTRY_BY_CITY_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			setCountryFields(resultSet, country);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Request from the data base error", e);
		} finally {
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statement);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
		return country;
	}
}
