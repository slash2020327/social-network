package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.file.Music;

public interface IMusicService {

	List<Music> getAll();

	Music getMusicById(Long id);

	void delete(Long id);

	void update(Music music);
	
	void create(Music music);

	List<Music> getAllMusicByProfileId(Long id);
}
