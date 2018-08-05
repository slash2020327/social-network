package by.qa.connectionproject.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.file.Photo;

public interface IAlbumDAO extends IAbstractDAO<Integer, Album> {

	void create(Album album) throws SQLException;
	Album getAlbumByPhotoId(Integer id);
	List<Photo> getAllPhotosByAlbumId(Integer id);
}
