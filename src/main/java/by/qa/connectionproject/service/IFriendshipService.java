package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.Friendship;

public interface IFriendshipService {

	List<Friendship> getAll();

	Friendship getFriendshipById(Long id);

	void delete(Long id);

	void update(Friendship friendship);
	
	void create(Friendship friendship);
	
	List<Friendship> getAllFriendshipByUserId(Long id);
}
