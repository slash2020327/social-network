package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.User;

public interface ICityDAO extends IAbstractDAO<Long, City> {

	City getEntityById(Long id);

	City getCityByUserId(Long id);

	List<City> getAll();

	void delete(Long id);

	void update(City city);

	void create(City city);

	List<User> getAllUsersByCityId(Long id);
}
