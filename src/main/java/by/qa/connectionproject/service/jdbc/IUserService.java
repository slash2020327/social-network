package by.qa.connectionproject.service.jdbc;

import java.util.List;

import by.qa.connectionproject.models.User;

public interface IUserService {

	List<User> getAll();
	User getUserById(Integer id);
}
