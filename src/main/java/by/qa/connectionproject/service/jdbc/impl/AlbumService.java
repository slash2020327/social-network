package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.AlbumDAO;
import by.qa.connectionproject.dao.jdbc.impl.PhotoDAO;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.file.Photo;
import by.qa.connectionproject.service.IAlbumService;

public class AlbumService implements IAlbumService {

	private AlbumDAO albumDAO = new AlbumDAO();
	private PhotoDAO photoDAO = new PhotoDAO();

	@Override
	public Album getAlbumById(Long id) {
		Album album = albumDAO.getEntityById(id);
		album.setPhotos(photoDAO.getAllPhotosByAlbumId(id));
		return album;
	}

	@Override
	public List<Album> getAll() {
		List<Album> albums = albumDAO.getAll();
		for (Album album : albums) {
			List<Photo> photos = photoDAO.getAllPhotosByAlbumId(album.getId());
			album.setPhotos(photos);
		}
		return albums;
	}

	@Override
	public void delete(Long id) {
		albumDAO.delete(id);
	}

	@Override
	public void update(Album album) {
		albumDAO.update(album);
	}

	@Override
	public void create(Album album) {
		albumDAO.create(album);
	}

	@Override
	public List<Album> getAllAlbumsByProfileId(Long id) {
		List<Album> albums = albumDAO.getAllAlbumsByProfileId(id);
		return albums;
	}

	@Override
	public Album getAlbumByPhotoId(Long id) {
		Album album = albumDAO.getAlbumByPhotoId(id);
		return album;
	}
}
