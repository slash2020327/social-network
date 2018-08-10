package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.Group;

public interface IGroupDAO extends IAbstractDAO<Long, Group> {

	List<Group> getAll();

	Group getEntityById(Long id);

	void delete(Long id);

	void update(Group group);
	
	void create(Group group);
	
	List<Group> getAllGroupsByProfileId(Long id);
}
