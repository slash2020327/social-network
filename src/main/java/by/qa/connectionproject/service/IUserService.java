package by.qa.connectionproject.service;

import java.util.List;

import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;

public interface IUserService {

	List<User> getAll();

	User getUserById(Long id);

	void delete(Long id);

	void update(User user);
	
	User getFromUserByPrivateMessageId(Long id);

	User getToUserByPrivateMessageId(Long id);

	User getUserOneByFriendshipId(Long id);

	User getUserTwoByFriendshipId(Long id);

	User getUserByDialogId(Long id);

	void create(User user, Profile profile);
}
