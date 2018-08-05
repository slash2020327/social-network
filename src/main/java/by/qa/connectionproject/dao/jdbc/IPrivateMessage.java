package by.qa.connectionproject.dao.jdbc;

import java.sql.SQLException;

import by.qa.connectionproject.models.PrivateMessage;

public interface IPrivateMessage extends IAbstractDAO<Integer, PrivateMessage> {

	void create(PrivateMessage privateMessage) throws SQLException;
}
