package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IUserDAO;
import by.qa.connectionproject.models.User;

public class UserDAO implements IUserDAO {

	@Override
	public User getEntityById(Long id) {
		IUserDAO userDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IUserDAO.class);
		User user = userDAO.getEntityById(id);
		return user;
	}

	@Override
	public List<User> getAll() {
		IUserDAO userDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IUserDAO.class);
		List<User> users = userDAO.getAll();
		return users;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IUserDAO userDAO = session.getMapper(IUserDAO.class);
			userDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(User user) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IUserDAO userDAO = session.getMapper(IUserDAO.class);
			userDAO.update(user);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public User getUserByDialogId(Long id) {
		IUserDAO userDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IUserDAO.class);
		User user = userDAO.getUserByDialogId(id);
		return user;
	}
	
	@Override
	public User getUserOneByFriendshipId(Long id) {
		IUserDAO userDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IUserDAO.class);
		User user = userDAO.getUserOneByFriendshipId(id);
		return user;
	}
	
	@Override
	public User getUserTwoByFriendshipId(Long id) {
		IUserDAO userDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IUserDAO.class);
		User user = userDAO.getUserTwoByFriendshipId(id);
		return user;
	}
	
	@Override
	public User getToUserByPrivateMessageId(Long id) {
		IUserDAO userDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IUserDAO.class);
		User user = userDAO.getToUserByPrivateMessageId(id);
		return user;
	}
	
	@Override
	public User getFromUserByPrivateMessageId(Long id) {
		IUserDAO userDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IUserDAO.class);
		User user = userDAO.getFromUserByPrivateMessageId(id);
		return user;
	}
}
