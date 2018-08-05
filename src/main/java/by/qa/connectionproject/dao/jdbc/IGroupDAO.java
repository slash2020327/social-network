package by.qa.connectionproject.dao.jdbc;

import java.util.List;
import by.qa.connectionproject.models.Group;
import by.qa.connectionproject.models.Profile;

public interface IGroupDAO extends IAbstractDAO<Integer, Group> {

	void create(Group group);

	List<Profile> getAllProfilesByGroupId(Integer id);
}
