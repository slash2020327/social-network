package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.GroupDAO;
import by.qa.connectionproject.dao.jdbc.impl.ProfileDAO;
import by.qa.connectionproject.models.Group;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.service.IGroupService;

public class GroupService implements IGroupService {

	private GroupDAO groupDAO = new GroupDAO();
	private ProfileDAO profileDAO = new ProfileDAO();

	@Override
	public Group getGroupById(Long id) {
		Group group = groupDAO.getEntityById(id);
		group.setProfiles(profileDAO.getAllProfilesByGroupId(id));
		return group;
	}

	@Override
	public List<Group> getAll() {
		List<Group> groups = groupDAO.getAll();
		for (Group group : groups) {
			List<Profile> profiles = profileDAO.getAllProfilesByGroupId(group.getId());
			group.setProfiles(profiles);
		}
		return groups;
	}

	@Override
	public void delete(Long id) {
		groupDAO.delete(id);
	}

	@Override
	public void update(Group group) {
		groupDAO.update(group);
	}

	@Override
	public void create(Group group) {
		groupDAO.create(group);
	}

	@Override
	public List<Group> getAllGroupsByProfileId(Long id) {
		List<Group> groups = groupDAO.getAllGroupsByProfileId(id);
		return groups;
	}
}
