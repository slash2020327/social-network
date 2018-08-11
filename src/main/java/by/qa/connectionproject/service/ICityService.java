package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.City;

public interface ICityService {

	City getCityById(Long id);

	City getCityByUserId(Long id);

	List<City> getAll();

	void delete(Long id);

	void update(City city);

	void create(City city);
}
