package by.qa.connectionproject.dao.jdbc;

import java.util.List;
import by.qa.connectionproject.models.Dialog;
import by.qa.connectionproject.models.PrivateMessage;

public interface IDialogDAO extends IAbstractDAO<Integer, Dialog> {

	void create(Dialog dialog);

	List<PrivateMessage> getAllMessagesByDialogId(Integer id);

	Dialog getDialogByMessageId(Integer id);
}
