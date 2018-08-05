package by.qa.connectionproject.service.jdbc;

import java.util.List;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;

public interface IUserService {

	void create(User user, Profile profile);

	List<User> getAll();

	User getUserById(Integer id);
}
