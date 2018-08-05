package by.qa.connectionproject.dao.jdbc;

import java.sql.SQLException;
import java.util.List;
import by.qa.connectionproject.models.Dialog;
import by.qa.connectionproject.models.Friendship;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;

public interface IUserDAO extends IAbstractDAO<Integer, User> {
	
	void create(User user, Profile profile) throws SQLException;
	User getFromUserByPrivateMessage(Integer id);
	User getToUserByPrivateMessage(Integer id);
	User getUserOneByFriendshipId(Integer id);
	User getUserTwoByFriendshipId(Integer id);
	User getUserByDialogId(Integer id);
	List<Dialog> getAllDialoguesByUserId(Integer id);
	List<Friendship> getAllFriendshipByUserId(Integer id);
}
