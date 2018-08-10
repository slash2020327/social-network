package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IAlbumDAO;
import by.qa.connectionproject.models.Album;

public class AlbumDAO implements IAlbumDAO {

	@Override
	public Album getEntityById(Long id) {
		IAlbumDAO albumDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IAlbumDAO.class);
		Album album = albumDAO.getEntityById(id);
		return album;
	}

	@Override
	public List<Album> getAll() {
		IAlbumDAO albumDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IAlbumDAO.class);
		List<Album> albums = albumDAO.getAll();
		return albums;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IAlbumDAO albumDAO = session.getMapper(IAlbumDAO.class);
			albumDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(Album album) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IAlbumDAO albumDAO = session.getMapper(IAlbumDAO.class);
			albumDAO.update(album);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(Album album) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IAlbumDAO albumDAO = session.getMapper(IAlbumDAO.class);
			albumDAO.create(album);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<Album> getAllAlbumsByProfileId(Long id) {
		IAlbumDAO albumDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IAlbumDAO.class);
		List<Album> albums = albumDAO.getAllAlbumsByProfileId(id);
		return albums;
	}
	
	@Override
	public Album getAlbumByPhotoId(Long id) {
		IAlbumDAO albumDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IAlbumDAO.class);
		Album album = albumDAO.getAlbumByPhotoId(id);
		return album;
	}
}
