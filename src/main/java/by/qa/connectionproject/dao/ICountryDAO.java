package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Country;

public interface ICountryDAO extends IAbstractDAO<Long, Country> {

	Country getEntityById(Long id);
	
	List<Country> getAll();
	
	void delete(Long id);

	void update(Country country);
	
	Country getCountryByCityId(Long id);

	void create(Country country);

	List<City> getAllCitiesByCountryId(Long id);
	
}
