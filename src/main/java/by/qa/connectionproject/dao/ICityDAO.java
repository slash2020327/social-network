package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.City;

public interface ICityDAO extends IAbstractDAO<Long, City> {

	City getEntityById(Long id);

	City getCityByUserId(Long id);

	List<City> getAll();

	void delete(Long id);

	void update(City city);

	void create(City city);
	
	List<City> getAllCitiesByCountryId(Long id);
}
