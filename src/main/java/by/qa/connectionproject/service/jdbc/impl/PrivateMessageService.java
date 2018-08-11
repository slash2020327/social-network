package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.PrivateMessageDAO;
import by.qa.connectionproject.models.PrivateMessage;
import by.qa.connectionproject.service.IPrivateMessageService;

public class PrivateMessageService implements IPrivateMessageService {

	private PrivateMessageDAO privateMessageDAO = new PrivateMessageDAO();

	@Override
	public PrivateMessage getMessageById(Long id) {
		PrivateMessage privateMessage = privateMessageDAO.getEntityById(id);
		return privateMessage;
	}

	@Override
	public List<PrivateMessage> getAll() {
		List<PrivateMessage> messages = privateMessageDAO.getAll();
		return messages;
	}

	@Override
	public void delete(Long id) {
		privateMessageDAO.delete(id);
	}

	@Override
	public void update(PrivateMessage message) {
		privateMessageDAO.update(message);
	}

	@Override
	public void create(PrivateMessage message) {
		privateMessageDAO.create(message);
	}

	public List<PrivateMessage> getAllMessagesByDialogId(Long id) {
		List<PrivateMessage> messages = privateMessageDAO.getAllMessagesByDialogId(id);
		return messages;
	}
}
