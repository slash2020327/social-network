package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IPrivateMessageDAO;
import by.qa.connectionproject.models.PrivateMessage;

public class PrivateMessageDAO implements IPrivateMessageDAO {

	@Override
	public PrivateMessage getEntityById(Long id) {
		IPrivateMessageDAO privateMessageDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IPrivateMessageDAO.class);
		PrivateMessage privateMessage = privateMessageDAO.getEntityById(id);
		return privateMessage;
	}

	@Override
	public List<PrivateMessage> getAll() {
		IPrivateMessageDAO privateMessageDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IPrivateMessageDAO.class);
		List<PrivateMessage> messages = privateMessageDAO.getAll();
		return messages;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IPrivateMessageDAO privateMessageDAO = session.getMapper(IPrivateMessageDAO.class);
			privateMessageDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(PrivateMessage message) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IPrivateMessageDAO privateMessageDAO = session.getMapper(IPrivateMessageDAO.class);
			privateMessageDAO.update(message);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(PrivateMessage message) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IPrivateMessageDAO videoDAO = session.getMapper(IPrivateMessageDAO.class);
			videoDAO.create(message);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public List<PrivateMessage> getAllMessagesByDialogId(Long id) {
		IPrivateMessageDAO privateMessageDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IPrivateMessageDAO.class);
		List<PrivateMessage> messages = privateMessageDAO.getAllMessagesByDialogId(id);
		return messages;
	}
}
