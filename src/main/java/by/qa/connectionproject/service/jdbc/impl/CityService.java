package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.CityDAO;
import by.qa.connectionproject.dao.jdbc.impl.CountryDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.service.ICityService;

public class CityService implements ICityService {

	private CityDAO cityDAO = new CityDAO();
	private CountryDAO countryDAO = new CountryDAO();

	@Override
	public City getCityById(Long id) {
		City city = cityDAO.getEntityById(id);
		city.setCountry(countryDAO.getCountryByCityId(id));
		return city;
	}

	@Override
	public List<City> getAll() {
		List<City> cities = cityDAO.getAll();
		for (City city:cities) {
			city.setCountry(countryDAO.getCountryByCityId(city.getId()));
		}
		return cities;
	}

	@Override
	public void delete(Long id) {
		cityDAO.delete(id);
	}

	@Override
	public void update(City city) {
		cityDAO.update(city);
	}

	@Override
	public void create(City city) {
		cityDAO.create(city);
	}

	@Override
	public City getCityByUserId(Long id) {
		City city = cityDAO.getCityByUserId(id);
		return city;
	}
}
