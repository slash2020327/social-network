package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.file.Photo;

public interface IPhotoDAO extends IAbstractDAO<Long, Photo> {

	List<Photo> getAll();

	Photo getEntityById(Long id);

	void delete(Long id);

	void update(Photo photo);
	
	void create(Photo photo);
	
	List<Photo> getAllPhotosByAlbumId(Long id);
}
