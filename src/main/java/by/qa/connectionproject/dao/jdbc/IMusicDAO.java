package by.qa.connectionproject.dao.jdbc;

import by.qa.connectionproject.models.file.Music;

public interface IMusicDAO extends IAbstractDAO<Integer, Music> {

	void create(Music music);

}
