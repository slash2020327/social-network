package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.Dialog;
import by.qa.connectionproject.service.IDialogService;

public class DialogService implements IDialogService {

	private IDialogService dialogService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IDialogService.class);
	
	@Override
	public Dialog getDialogById(Long id) {
		Dialog dialog = dialogService.getDialogById(id);
		return dialog;
	}

	@Override
	public List<Dialog> getAll() {
		List<Dialog> dialogs = dialogService.getAll();
		return dialogs;
	}

	@Override
	public void delete(Long id) {
			dialogService.delete(id);
	}
	
	@Override
	public void update(Dialog dialog) {
			dialogService.update(dialog);
	}
	
	@Override
	public void create(Dialog dialog) {
			dialogService.create(dialog);
	}
	
	@Override
	public List<Dialog> getAllDialoguesByUserId(Long id) {
		List<Dialog> dialogues = dialogService.getAllDialoguesByUserId(id);
		return dialogues;
	}
	
	@Override
	public Dialog getDialogByMessageId(Long id) {
		Dialog dialog = dialogService.getDialogByMessageId(id);
		return dialog;
	}
}
