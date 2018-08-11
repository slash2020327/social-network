package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;
import by.qa.connectionproject.service.IUserService;

public class UserService implements IUserService {

	private IUserService userService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IUserService.class);

	@Override
	public User getUserById(Long id) {
		User user = userService.getUserById(id);
		return user;
	}

	@Override
	public List<User> getAll() {
		List<User> users = userService.getAll();
		return users;
	}

	@Override
	public void delete(Long id) {
		userService.delete(id);
	}

	@Override
	public void update(User user) {
		userService.update(user);
	}

	@Override
	public User getUserByDialogId(Long id) {
		User user = userService.getUserByDialogId(id);
		return user;
	}

	@Override
	public User getUserOneByFriendshipId(Long id) {
		User user = userService.getUserOneByFriendshipId(id);
		return user;
	}

	@Override
	public User getUserTwoByFriendshipId(Long id) {
		User user = userService.getUserTwoByFriendshipId(id);
		return user;
	}

	@Override
	public User getToUserByPrivateMessageId(Long id) {
		User user = userService.getToUserByPrivateMessageId(id);
		return user;
	}

	@Override
	public User getFromUserByPrivateMessageId(Long id) {
		User user = userService.getFromUserByPrivateMessageId(id);
		return user;
	}
	
	@Override
	public void create(User user, Profile profile) {
		throw new java.lang.UnsupportedOperationException("Not supported yet");
	}
}
