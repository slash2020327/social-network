package by.qa.connectionproject.dao.mybatis.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import by.qa.connectionproject.dao.IPhotoDAO;
import by.qa.connectionproject.models.file.Photo;

public class PhotoDAO implements IPhotoDAO {

	@Override
	public Photo getEntityById(Long id) {
		IPhotoDAO photoDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IPhotoDAO.class);
		Photo photo = photoDAO.getEntityById(id);
		return photo;
	}

	@Override
	public List<Photo> getAll() {
		IPhotoDAO photoDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IPhotoDAO.class);
		List<Photo> photos = photoDAO.getAll();
		return photos;
	}

	@Override
	public void delete(Long id) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IPhotoDAO photoDAO = session.getMapper(IPhotoDAO.class);
			photoDAO.delete(id);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(Photo photo) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IPhotoDAO photoDAO = session.getMapper(IPhotoDAO.class);
			photoDAO.update(photo);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void create(Photo photo) {
		SqlSession session = null;
		try {
			session = MybatisUtil.getSqlSessionFactory().openSession();
			IPhotoDAO photoDAO = session.getMapper(IPhotoDAO.class);
			photoDAO.create(photo);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<Photo> getAllPhotosByAlbumId(Long id) {
		IPhotoDAO photoDAO = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IPhotoDAO.class);
		List<Photo> photos = photoDAO.getAllPhotosByAlbumId(id);
		return photos;
	}
}
