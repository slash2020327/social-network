package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IProfileDAO;
import by.qa.connectionproject.models.Profile;

public class ProfileDAO implements IProfileDAO {

	@Override
	public Profile getEntityById(Long id) {
		IProfileDAO profileDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IProfileDAO.class);
		Profile profile = profileDAO.getEntityById(id);
		return profile;
	}

	@Override
	public List<Profile> getAll() {
		IProfileDAO profileDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IProfileDAO.class);
		List<Profile> profiles = profileDAO.getAll();
		return profiles;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IProfileDAO profileDAO = session.getMapper(IProfileDAO.class);
			profileDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}

	@Override
	public void update(Profile profile) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IProfileDAO profileDAO = session.getMapper(IProfileDAO.class);
			profileDAO.update(profile);
			session.commit();
		} finally {
			session.close();
		}
	}

	@Override
	public Profile getProfileByMusicId(Long id) {
		IProfileDAO profileDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IProfileDAO.class);
		Profile profile = profileDAO.getProfileByMusicId(id);
		return profile;
	}

	@Override
	public Profile getProfileByVideoId(Long id) {
		IProfileDAO profileDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IProfileDAO.class);
		Profile profile = profileDAO.getProfileByVideoId(id);
		return profile;
	}

	@Override
	public Profile getProfileByAlbumId(Long id) {
		IProfileDAO profileDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IProfileDAO.class);
		Profile profile = profileDAO.getProfileByAlbumId(id);
		return profile;
	}

	@Override
	public Profile getProfileByUserId(Long id) {
		IProfileDAO profileDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IProfileDAO.class);
		Profile profile = profileDAO.getProfileByUserId(id);
		return profile;
	}
	
	@Override
	public List<Profile> getAllProfilesByGroupId(Long id) {
		IProfileDAO profileDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IProfileDAO.class);
		List<Profile> profiles = profileDAO.getAllProfilesByGroupId(id);
		return profiles;
	}
}
