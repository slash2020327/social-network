package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.Friendship;
import by.qa.connectionproject.service.IFriendshipService;

public class FriendshipService implements IFriendshipService {

	private IFriendshipService friendshipService = MybatisUtil.getSqlSessionFactory().openSession()
			.getMapper(IFriendshipService.class);

	@Override
	public Friendship getFriendshipById(Long id) {
		Friendship friendship = friendshipService.getFriendshipById(id);
		return friendship;
	}

	@Override
	public List<Friendship> getAll() {
		List<Friendship> friendshipList = friendshipService.getAll();
		return friendshipList;
	}

	@Override
	public void delete(Long id) {
		friendshipService.delete(id);
	}

	@Override
	public void update(Friendship friendship) {
		friendshipService.update(friendship);
	}

	@Override
	public void create(Friendship friendship) {
		friendshipService.create(friendship);
	}

	@Override
	public List<Friendship> getAllFriendshipByUserId(Long id) {
		List<Friendship> friendshipList = friendshipService.getAllFriendshipByUserId(id);
		return friendshipList;
	}
}
