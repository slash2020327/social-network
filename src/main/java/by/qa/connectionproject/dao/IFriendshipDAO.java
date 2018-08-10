package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.Friendship;

public interface IFriendshipDAO extends IAbstractDAO<Long, Friendship> {

	List<Friendship> getAll();

	Friendship getEntityById(Long id);

	void delete(Long id);

	void update(Friendship friendship);
	
	void create(Friendship friendship);
	
	List<Friendship> getAllFriendshipByUserId(Long id);
}
