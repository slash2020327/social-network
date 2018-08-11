package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.Country;
import by.qa.connectionproject.service.ICountryService;

public class CountryService implements ICountryService {

	private ICountryService countryService = MybatisUtil.getSqlSessionFactory().openSession()
			.getMapper(ICountryService.class);

	@Override
	public Country getCountryById(Long id) {
		Country country = countryService.getCountryById(id);
		return country;
	}

	@Override
	public List<Country> getAll() {
		List<Country> countries = countryService.getAll();
		return countries;
	}

	@Override
	public void delete(Long id) {
		countryService.delete(id);
	}

	@Override
	public void update(Country country) {
		countryService.update(country);
	}

	@Override
	public void create(Country country) {
		countryService.create(country);
	}

	@Override
	public Country getCountryByCityId(Long id) {
		Country country = countryService.getCountryByCityId(id);
		return country;
	}
}
