package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.ICountryDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Country;

public class CountryDAO implements ICountryDAO {

	@Override
	public Country getEntityById(Long id) {
		ICountryDAO countryDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(ICountryDAO.class);
		Country country = countryDAO.getEntityById(id);
		return country;
	}

	@Override
	public List<Country> getAll() {
		ICountryDAO countryDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(ICountryDAO.class);
		List<Country> countries = countryDAO.getAll();
		return countries;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			ICountryDAO countryDAO = session.getMapper(ICountryDAO.class);
			countryDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(Country country) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			ICountryDAO countryDAO = session.getMapper(ICountryDAO.class);
			countryDAO.update(country);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(Country country) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			ICountryDAO countryDAO = session.getMapper(ICountryDAO.class);
			countryDAO.create(country);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public Country getCountryByCityId(Long id) {
		ICountryDAO countryDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(ICountryDAO.class);
		Country country = countryDAO.getCountryByCityId(id);
		return country;
	}
	
	@Override
	public List<City> getAllCitiesByCountryId(Long id) {
		throw new java.lang.UnsupportedOperationException("Not supported yet");
	}
}
