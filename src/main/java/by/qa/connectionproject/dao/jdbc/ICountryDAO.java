package by.qa.connectionproject.dao.jdbc;
import java.sql.SQLException;
import java.util.List;

import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Country;

public interface ICountryDAO extends IAbstractDAO<Integer, Country> {

	Country getCountryByCityId(Integer id);
	void create(Country country) throws SQLException;
	List<City> getAllCitiesByCountryId(Integer id);
}
