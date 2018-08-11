package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.PhotoDAO;
import by.qa.connectionproject.models.file.Photo;
import by.qa.connectionproject.service.IPhotoService;

public class PhotoService implements IPhotoService {

	private PhotoDAO photoDAO = new PhotoDAO();

	@Override
	public Photo getPhotoById(Long id) {
		Photo photo = photoDAO.getEntityById(id);
		return photo;
	}

	@Override
	public List<Photo> getAll() {
		List<Photo> photos = photoDAO.getAll();
		return photos;
	}

	@Override
	public void delete(Long id) {
		photoDAO.delete(id);
	}

	@Override
	public void update(Photo photo) {
		photoDAO.update(photo);
	}

	@Override
	public void create(Photo photo) {
		photoDAO.create(photo);
	}

	@Override
	public List<Photo> getAllPhotosByAlbumId(Long id) {
		List<Photo> photos = photoDAO.getAllPhotosByAlbumId(id);
		return photos;
	}
}
