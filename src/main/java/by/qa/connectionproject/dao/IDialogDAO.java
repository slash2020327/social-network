package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.Dialog;

public interface IDialogDAO extends IAbstractDAO<Long, Dialog> {

	List<Dialog> getAll();

	Dialog getEntityById(Long id);

	void delete(Long id);

	void update(Dialog dialog);
	
	void create(Dialog dialog);

	Dialog getDialogByMessageId(Long id);
	
	List<Dialog> getAllDialoguesByUserId(Long id);
}
