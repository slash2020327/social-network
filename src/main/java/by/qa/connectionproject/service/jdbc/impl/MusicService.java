package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.MusicDAO;
import by.qa.connectionproject.models.file.Music;
import by.qa.connectionproject.service.IMusicService;

public class MusicService implements IMusicService {

	private MusicDAO musicDAO = new MusicDAO();

	@Override
	public Music getMusicById(Long id) {
		Music music = musicDAO.getEntityById(id);
		return music;
	}

	@Override
	public List<Music> getAll() {
		List<Music> musicList = musicDAO.getAll();
		return musicList;
	}

	@Override
	public void delete(Long id) {
		musicDAO.delete(id);
	}

	@Override
	public void update(Music music) {
		musicDAO.update(music);
	}

	@Override
	public void create(Music music) {
		musicDAO.create(music);
	}

	@Override
	public List<Music> getAllMusicByProfileId(Long id) {
		List<Music> musicList = musicDAO.getAllMusicByProfileId(id);
		return musicList;
	}
}
