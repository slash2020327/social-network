package by.qa.connectionproject.dao.jdbc;

import by.qa.connectionproject.models.PrivateMessage;

public interface IPrivateMessage extends IAbstractDAO<Integer, PrivateMessage> {

	void create(PrivateMessage privateMessage);
}
