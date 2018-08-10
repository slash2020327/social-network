package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.file.Music;

public interface IMusicDAO extends IAbstractDAO<Long, Music> {

	List<Music> getAll();

	Music getEntityById(Long id);

	void delete(Long id);

	void update(Music music);
	
	void create(Music music);

	List<Music> getAllMusicByProfileId(Long id);
}
