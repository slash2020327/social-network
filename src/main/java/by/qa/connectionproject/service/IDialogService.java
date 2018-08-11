package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.Dialog;

public interface IDialogService {

	List<Dialog> getAll();

	Dialog getDialogById(Long id);

	void delete(Long id);

	void update(Dialog dialog);
	
	void create(Dialog dialog);

	Dialog getDialogByMessageId(Long id);
	
	List<Dialog> getAllDialoguesByUserId(Long id);
}
