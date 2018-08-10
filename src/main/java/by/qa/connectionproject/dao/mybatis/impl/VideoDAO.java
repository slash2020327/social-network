package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IVideoDAO;
import by.qa.connectionproject.models.file.Video;

public class VideoDAO implements IVideoDAO {

	@Override
	public Video getEntityById(Long id) {
		IVideoDAO videoDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IVideoDAO.class);
		Video video = videoDAO.getEntityById(id);
		return video;
	}

	@Override
	public List<Video> getAll() {
		IVideoDAO videoDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IVideoDAO.class);
		List<Video> videos = videoDAO.getAll();
		return videos;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IVideoDAO videoDAO = session.getMapper(IVideoDAO.class);
			videoDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(Video video) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IVideoDAO videoDAO = session.getMapper(IVideoDAO.class);
			videoDAO.update(video);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(Video video) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IVideoDAO videoDAO = session.getMapper(IVideoDAO.class);
			videoDAO.create(video);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<Video> getAllVideoByProfileId(Long id) {
		IVideoDAO videoDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IVideoDAO.class);
		List<Video> videos = videoDAO.getAllVideoByProfileId(id);
		return videos;
	}
}
