package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IMusicDAO;
import by.qa.connectionproject.models.file.Music;

public class MusicDAO implements IMusicDAO {

	@Override
	public Music getEntityById(Long id) {
		IMusicDAO musicDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IMusicDAO.class);
		Music music = musicDAO.getEntityById(id);
		return music;
	}

	@Override
	public List<Music> getAll() {
		IMusicDAO musicDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IMusicDAO.class);
		List<Music> musicList = musicDAO.getAll();
		return musicList;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IMusicDAO musicDAO = session.getMapper(IMusicDAO.class);
			musicDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(Music music) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IMusicDAO musicDAO = session.getMapper(IMusicDAO.class);
			musicDAO.update(music);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(Music music) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IMusicDAO musicDAO = session.getMapper(IMusicDAO.class);
			musicDAO.create(music);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<Music> getAllMusicByProfileId(Long id) {
		IMusicDAO musicDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IMusicDAO.class);
		List<Music> musicList = musicDAO.getAllMusicByProfileId(id);
		return musicList;
	}
}
