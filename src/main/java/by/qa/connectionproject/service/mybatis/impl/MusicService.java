package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.file.Music;
import by.qa.connectionproject.service.IMusicService;

public class MusicService implements IMusicService {

	private IMusicService musicService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IMusicService.class);

	@Override
	public Music getMusicById(Long id) {
		Music music = musicService.getMusicById(id);
		return music;
	}

	@Override
	public List<Music> getAll() {
		List<Music> musicList = musicService.getAll();
		return musicList;
	}

	@Override
	public void delete(Long id) {
		musicService.delete(id);
	}

	@Override
	public void update(Music music) {
		musicService.update(music);
	}

	@Override
	public void create(Music music) {
		musicService.create(music);
	}

	@Override
	public List<Music> getAllMusicByProfileId(Long id) {
		List<Music> musicList = musicService.getAllMusicByProfileId(id);
		return musicList;
	}
}
