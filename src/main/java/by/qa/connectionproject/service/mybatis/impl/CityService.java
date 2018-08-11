package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.service.ICityService;

public class CityService implements ICityService {

	private ICityService cityService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(ICityService.class);

	@Override
	public City getCityById(Long id) {
		City city = cityService.getCityById(id);
		return city;
	}

	@Override
	public List<City> getAll() {
		List<City> cities = cityService.getAll();
		return cities;
	}

	@Override
	public void delete(Long id) {
		cityService.delete(id);
	}

	@Override
	public void update(City city) {
		cityService.update(city);
	}

	@Override
	public void create(City city) {
		cityService.create(city);
	}

	@Override
	public City getCityByUserId(Long id) {
		City city = cityService.getCityByUserId(id);
		return city;
	}
}
