package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.PrivateMessage;

public interface IPrivateMessageService {

	List<PrivateMessage> getAll();

	PrivateMessage getMessageById(Long id);

	void delete(Long id);

	void update(PrivateMessage privateMessage);
	
	void create(PrivateMessage privateMessage);
	
	List<PrivateMessage> getAllMessagesByDialogId(Long id);
}
