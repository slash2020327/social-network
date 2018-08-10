package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IFriendshipDAO;
import by.qa.connectionproject.models.Friendship;

public class FriendshipDAO implements IFriendshipDAO {

	@Override
	public Friendship getEntityById(Long id) {
		IFriendshipDAO friendshipDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IFriendshipDAO.class);
		Friendship friendship = friendshipDAO.getEntityById(id);
		return friendship;
	}

	@Override
	public List<Friendship> getAll() {
		IFriendshipDAO friendshipDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IFriendshipDAO.class);
		List<Friendship> friendshipList = friendshipDAO.getAll();
		return friendshipList;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IFriendshipDAO friendshipDAO = session.getMapper(IFriendshipDAO.class);
			friendshipDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(Friendship friendship) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IFriendshipDAO friendshipDAO = session.getMapper(IFriendshipDAO.class);
			friendshipDAO.update(friendship);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(Friendship friendship) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IFriendshipDAO friendshipDAO = session.getMapper(IFriendshipDAO.class);
			friendshipDAO.create(friendship);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<Friendship> getAllFriendshipByUserId(Long id) {
		IFriendshipDAO friendshipDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IFriendshipDAO.class);
		List<Friendship> friendshipList = friendshipDAO.getAllFriendshipByUserId(id);
		return friendshipList;
	}
}
