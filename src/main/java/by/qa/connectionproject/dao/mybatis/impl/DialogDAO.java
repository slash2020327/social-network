package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IDialogDAO;
import by.qa.connectionproject.models.Dialog;

public class DialogDAO implements IDialogDAO {

	@Override
	public Dialog getEntityById(Long id) {
		IDialogDAO dialogDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IDialogDAO.class);
		Dialog dialog = dialogDAO.getEntityById(id);
		return dialog;
	}

	@Override
	public List<Dialog> getAll() {
		IDialogDAO dialogDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IDialogDAO.class);
		List<Dialog> dialogs = dialogDAO.getAll();
		return dialogs;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IDialogDAO dialogDAO = session.getMapper(IDialogDAO.class);
			dialogDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(Dialog dialog) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IDialogDAO dialogDAO = session.getMapper(IDialogDAO.class);
			dialogDAO.update(dialog);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(Dialog dialog) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IDialogDAO dialogDAO = session.getMapper(IDialogDAO.class);
			dialogDAO.create(dialog);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<Dialog> getAllDialoguesByUserId(Long id) {
		IDialogDAO dialogDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IDialogDAO.class);
		List<Dialog> dialogues = dialogDAO.getAllDialoguesByUserId(id);
		return dialogues;
	}
	
	@Override
	public Dialog getDialogByMessageId(Long id) {
		IDialogDAO dialogDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IDialogDAO.class);
		Dialog dialog = dialogDAO.getDialogByMessageId(id);
		return dialog;
	}
}
