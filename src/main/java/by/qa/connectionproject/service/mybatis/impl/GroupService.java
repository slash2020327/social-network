package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.Group;
import by.qa.connectionproject.service.IGroupService;

public class GroupService implements IGroupService {

	private IGroupService groupService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IGroupService.class);

	@Override
	public Group getGroupById(Long id) {
		Group group = groupService.getGroupById(id);
		return group;
	}

	@Override
	public List<Group> getAll() {
		List<Group> groups = groupService.getAll();
		return groups;
	}

	@Override
	public void delete(Long id) {
		groupService.delete(id);
	}

	@Override
	public void update(Group group) {
		groupService.update(group);
	}

	@Override
	public void create(Group group) {
		groupService.create(group);
	}

	@Override
	public List<Group> getAllGroupsByProfileId(Long id) {
		List<Group> groups = groupService.getAllGroupsByProfileId(id);
		return groups;
	}
}
