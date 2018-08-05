package by.qa.connectionproject.dao.jdbc;

import java.sql.SQLException;

import by.qa.connectionproject.models.Friendship;

public interface IFriendshipDAO extends IAbstractDAO<Integer, Friendship> {

	void create(Friendship friendship) throws SQLException;
}
