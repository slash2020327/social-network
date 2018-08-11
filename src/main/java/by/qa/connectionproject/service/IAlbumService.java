package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.Album;

public interface IAlbumService {

	List<Album> getAll();

	Album getAlbumById(Long id);

	void delete(Long id);

	void update(Album album);
	
	void create(Album album);

	Album getAlbumByPhotoId(Long id);
	
	List<Album> getAllAlbumsByProfileId(Long id);
}
