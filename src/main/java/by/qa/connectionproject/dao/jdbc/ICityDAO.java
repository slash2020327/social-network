package by.qa.connectionproject.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.User;

public interface ICityDAO extends IAbstractDAO<Integer, City>{

	City getCityByUserId(Integer id);
	void create(City city) throws SQLException;
	List<User> getAllUsersByCityId(Integer id);
}
