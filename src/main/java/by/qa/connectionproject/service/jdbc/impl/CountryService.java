package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.CityDAO;
import by.qa.connectionproject.dao.jdbc.impl.CountryDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Country;
import by.qa.connectionproject.service.ICountryService;

public class CountryService implements ICountryService {

	private CountryDAO countryDAO = new CountryDAO();
	private CityDAO cityDAO = new CityDAO();
	
	@Override
	public Country getCountryById(Long id) {
		Country country = countryDAO.getEntityById(id);
		country.setCities(cityDAO.getAllCitiesByCountryId(id));
		return country;
	}

	@Override
	public List<Country> getAll() {
		List<Country> countries = countryDAO.getAll();
		for (Country country : countries) {
			List<City> cities = cityDAO.getAllCitiesByCountryId(country.getId());
			country.setCities(cities);
		}
		return countries;
	}

	@Override
	public void delete(Long id) {
		countryDAO.delete(id);
	}

	@Override
	public void update(Country country) {
		countryDAO.update(country);
	}

	@Override
	public void create(Country country) {
		countryDAO.create(country);
	}

	@Override
	public Country getCountryByCityId(Long id) {
		Country country = countryDAO.getCountryByCityId(id);
		return country;
	}
}
