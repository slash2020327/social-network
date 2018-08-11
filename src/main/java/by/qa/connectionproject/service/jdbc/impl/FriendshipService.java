package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.FriendshipDAO;
import by.qa.connectionproject.models.Friendship;
import by.qa.connectionproject.service.IFriendshipService;

public class FriendshipService implements IFriendshipService {

	private FriendshipDAO friendshipDAO = new FriendshipDAO();

	@Override
	public Friendship getFriendshipById(Long id) {
		Friendship friendship = friendshipDAO.getEntityById(id);
		return friendship;
	}

	@Override
	public List<Friendship> getAll() {
		List<Friendship> friendshipList = friendshipDAO.getAll();
		return friendshipList;
	}

	@Override
	public void delete(Long id) {
		friendshipDAO.delete(id);
	}

	@Override
	public void update(Friendship friendship) {
		friendshipDAO.update(friendship);
	}

	@Override
	public void create(Friendship friendship) {
		friendshipDAO.create(friendship);
	}

	@Override
	public List<Friendship> getAllFriendshipByUserId(Long id) {
		List<Friendship> friendshipList = friendshipDAO.getAllFriendshipByUserId(id);
		return friendshipList;
	}
}
