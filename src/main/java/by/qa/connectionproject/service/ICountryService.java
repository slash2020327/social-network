package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.Country;

public interface ICountryService {

	Country getCountryById(Long id);
	
	List<Country> getAll();
	
	void delete(Long id);

	void update(Country country);
	
	Country getCountryByCityId(Long id);

	void create(Country country);
	
}
