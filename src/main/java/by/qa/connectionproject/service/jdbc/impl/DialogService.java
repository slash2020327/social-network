package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.DialogDAO;
import by.qa.connectionproject.dao.jdbc.impl.PrivateMessageDAO;
import by.qa.connectionproject.models.Dialog;
import by.qa.connectionproject.models.PrivateMessage;
import by.qa.connectionproject.service.IDialogService;

public class DialogService implements IDialogService {

	private DialogDAO dialogDAO = new DialogDAO();
	private PrivateMessageDAO messageDAO = new PrivateMessageDAO();

	@Override
	public Dialog getDialogById(Long id) {
		Dialog dialog = dialogDAO.getEntityById(id);
		dialog.setPrivateMessages(messageDAO.getAllMessagesByDialogId(id));
		return dialog;
	}

	@Override
	public List<Dialog> getAll() {
		List<Dialog> dialogues = dialogDAO.getAll();
		for (Dialog dialog : dialogues) {
			List<PrivateMessage> privateMessages = messageDAO.getAllMessagesByDialogId(dialog.getId());
			dialog.setPrivateMessages(privateMessages);
		}
		return dialogues;
	}

	@Override
	public void delete(Long id) {
		dialogDAO.delete(id);
	}

	@Override
	public void update(Dialog dialog) {
		dialogDAO.update(dialog);
	}

	@Override
	public void create(Dialog dialog) {
		dialogDAO.create(dialog);
	}

	@Override
	public List<Dialog> getAllDialoguesByUserId(Long id) {
		List<Dialog> dialogues = dialogDAO.getAllDialoguesByUserId(id);
		return dialogues;
	}

	@Override
	public Dialog getDialogByMessageId(Long id) {
		Dialog dialog = dialogDAO.getDialogByMessageId(id);
		return dialog;
	}
}
