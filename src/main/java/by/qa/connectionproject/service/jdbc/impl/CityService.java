package by.qa.connectionproject.service.jdbc.impl;

import by.qa.connectionproject.dao.jdbc.impl.CityDAO;
import by.qa.connectionproject.dao.jdbc.impl.CountryDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.service.jdbc.ICityService;

public class CityService implements ICityService {

	private CityDAO cityDAO = new CityDAO();
	private CountryDAO countryDAO = new CountryDAO();
	
	@Override
	public City getCityById(Long id) {
		City city = cityDAO.getEntityById(id);
		city.setCountry(countryDAO.getCountryByCityId(id));
		return city;
	}
}
