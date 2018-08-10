package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.ICityDAO;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.User;

public class CityDAO implements ICityDAO {

	@Override
	public City getEntityById(Long id) {
		ICityDAO cityDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(ICityDAO.class);
		City city = cityDAO.getEntityById(id);
		return city;
	}

	@Override
	public List<City> getAll() {
		ICityDAO cityDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(ICityDAO.class);
		List<City> cities = cityDAO.getAll();
		return cities;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			ICityDAO cityDAO = session.getMapper(ICityDAO.class);
			cityDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(City city) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			ICityDAO cityDAO = session.getMapper(ICityDAO.class);
			cityDAO.update(city);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(City city) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			ICityDAO cityDAO = session.getMapper(ICityDAO.class);
			cityDAO.create(city);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public City getCityByUserId(Long id) {
		ICityDAO cityDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(ICityDAO.class);
		City city = cityDAO.getCityByUserId(id);
		return city;
	}
	
	@Override
	public List<User> getAllUsersByCityId(Long id) {
		throw new java.lang.UnsupportedOperationException("Not supported yet");
	}
}
