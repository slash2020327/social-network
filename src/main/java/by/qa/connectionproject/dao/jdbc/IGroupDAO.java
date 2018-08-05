package by.qa.connectionproject.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import by.qa.connectionproject.models.Group;
import by.qa.connectionproject.models.Profile;

public interface IGroupDAO extends IAbstractDAO<Integer, Group> {

	void create(Group group) throws SQLException;
	List<Profile> getAllProfilesByGroupId(Integer id);
}
