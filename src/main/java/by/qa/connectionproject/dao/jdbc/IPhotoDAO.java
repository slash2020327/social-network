package by.qa.connectionproject.dao.jdbc;

import by.qa.connectionproject.models.file.Photo;

public interface IPhotoDAO extends IAbstractDAO<Integer, Photo> {

	void create(Photo photo);
}
