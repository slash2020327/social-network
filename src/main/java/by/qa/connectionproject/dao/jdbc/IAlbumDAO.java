package by.qa.connectionproject.dao.jdbc;

import java.util.List;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.file.Photo;

public interface IAlbumDAO extends IAbstractDAO<Integer, Album> {

	void create(Album album);

	Album getAlbumByPhotoId(Integer id);

	List<Photo> getAllPhotosByAlbumId(Integer id);
}
