package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IGroupDAO;
import by.qa.connectionproject.models.Group;

public class GroupDAO implements IGroupDAO {

	@Override
	public Group getEntityById(Long id) {
		IGroupDAO groupDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IGroupDAO.class);
		Group group = groupDAO.getEntityById(id);
		return group;
	}

	@Override
	public List<Group> getAll() {
		IGroupDAO groupDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IGroupDAO.class);
		List<Group> groups = groupDAO.getAll();
		return groups;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IGroupDAO groupDAO = session.getMapper(IGroupDAO.class);
			groupDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(Group group) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IGroupDAO groupDAO = session.getMapper(IGroupDAO.class);
			groupDAO.update(group);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(Group group) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IGroupDAO groupDAO = session.getMapper(IGroupDAO.class);
			groupDAO.create(group);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<Group> getAllGroupsByProfileId(Long id) {
		IGroupDAO groupDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IGroupDAO.class);
		List<Group> groups = groupDAO.getAllGroupsByProfileId(id);
		return groups;
	}
}
