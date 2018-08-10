package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.Album;

public interface IAlbumDAO extends IAbstractDAO<Long, Album> {

	List<Album> getAll();

	Album getEntityById(Long id);

	void delete(Long id);

	void update(Album album);
	
	void create(Album album);

	Album getAlbumByPhotoId(Long id);
	
	List<Album> getAllAlbumsByProfileId(Long id);
}
