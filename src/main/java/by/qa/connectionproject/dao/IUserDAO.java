package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.User;

public interface IUserDAO extends IAbstractDAO<Long, User> {

	List<User> getAll();

	User getEntityById(Long id);

	void delete(Long id);

	void update(User user);
	
	User getFromUserByPrivateMessageId(Long id);

	User getToUserByPrivateMessageId(Long id);

	User getUserOneByFriendshipId(Long id);

	User getUserTwoByFriendshipId(Long id);

	User getUserByDialogId(Long id);

}
