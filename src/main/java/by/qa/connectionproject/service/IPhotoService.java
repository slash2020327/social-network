package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.file.Photo;

public interface IPhotoService {

	List<Photo> getAll();

	Photo getPhotoById(Long id);

	void delete(Long id);

	void update(Photo photo);
	
	void create(Photo photo);
	
	List<Photo> getAllPhotosByAlbumId(Long id);
}
