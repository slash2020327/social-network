package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.service.IAlbumService;

public class AlbumService implements IAlbumService {

	private IAlbumService albumService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IAlbumService.class);

	@Override
	public Album getAlbumById(Long id) {
		Album album = albumService.getAlbumById(id);
		return album;
	}

	@Override
	public List<Album> getAll() {
		List<Album> albums = albumService.getAll();
		return albums;
	}

	@Override
	public void delete(Long id) {
		albumService.delete(id);
	}

	@Override
	public void update(Album album) {
		albumService.update(album);
	}

	@Override
	public void create(Album album) {
		albumService.create(album);
	}

	@Override
	public List<Album> getAllAlbumsByProfileId(Long id) {
		List<Album> albums = albumService.getAllAlbumsByProfileId(id);
		return albums;
	}

	@Override
	public Album getAlbumByPhotoId(Long id) {
		Album album = albumService.getAlbumByPhotoId(id);
		return album;
	}
}
