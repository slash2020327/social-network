package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.file.Photo;
import by.qa.connectionproject.service.IPhotoService;

public class PhotoService implements IPhotoService {

	private IPhotoService photoService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IPhotoService.class);

	@Override
	public Photo getPhotoById(Long id) {
		Photo photo = photoService.getPhotoById(id);
		return photo;
	}

	@Override
	public List<Photo> getAll() {
		List<Photo> photos = photoService.getAll();
		return photos;
	}

	@Override
	public void delete(Long id) {
		photoService.delete(id);
	}

	@Override
	public void update(Photo photo) {
		photoService.update(photo);
	}

	@Override
	public void create(Photo photo) {
		photoService.create(photo);
	}

	@Override
	public List<Photo> getAllPhotosByAlbumId(Long id) {
		List<Photo> photos = photoService.getAllPhotosByAlbumId(id);
		return photos;
	}
}
