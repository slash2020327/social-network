package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.Group;

public interface IGroupService {

	List<Group> getAll();

	Group getGroupById(Long id);

	void delete(Long id);

	void update(Group group);
	
	void create(Group group);
	
	List<Group> getAllGroupsByProfileId(Long id);
}
