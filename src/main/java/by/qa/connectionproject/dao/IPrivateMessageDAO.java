package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.PrivateMessage;

public interface IPrivateMessageDAO extends IAbstractDAO<Long, PrivateMessage> {

	List<PrivateMessage> getAll();

	PrivateMessage getEntityById(Long id);

	void delete(Long id);

	void update(PrivateMessage privateMessage);
	
	void create(PrivateMessage privateMessage);
	
	List<PrivateMessage> getAllMessagesByDialogId(Long id);
}
