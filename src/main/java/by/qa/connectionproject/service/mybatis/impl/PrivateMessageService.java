package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.PrivateMessage;
import by.qa.connectionproject.service.IPrivateMessageService;

public class PrivateMessageService implements IPrivateMessageService {

	private IPrivateMessageService privateMessageService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IPrivateMessageService.class);

	@Override
	public PrivateMessage getMessageById(Long id) {
		PrivateMessage privateMessage = privateMessageService.getMessageById(id);
		return privateMessage;
	}

	@Override
	public List<PrivateMessage> getAll() {
		List<PrivateMessage> messages = privateMessageService.getAll();
		return messages;
	}

	@Override
	public void delete(Long id) {
		privateMessageService.delete(id);
	}

	@Override
	public void update(PrivateMessage message) {
		privateMessageService.update(message);
	}

	@Override
	public void create(PrivateMessage message) {
		privateMessageService.create(message);
	}

	public List<PrivateMessage> getAllMessagesByDialogId(Long id) {
		List<PrivateMessage> messages = privateMessageService.getAllMessagesByDialogId(id);
		return messages;
	}
}
