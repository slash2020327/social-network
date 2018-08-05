package by.qa.connectionproject.dao.jdbc;

import by.qa.connectionproject.models.file.Video;

public interface IVideoDAO extends IAbstractDAO<Integer, Video> {

	void create(Video video);
}
