package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.file.Video;
import by.qa.connectionproject.service.IVideoService;

public class VideoService implements IVideoService {

	private IVideoService videoService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IVideoService.class);

	@Override
	public Video getVideoById(Long id) {
		Video video = videoService.getVideoById(id);
		return video;
	}

	@Override
	public List<Video> getAll() {
		List<Video> videos = videoService.getAll();
		return videos;
	}

	@Override
	public void delete(Long id) {
		videoService.delete(id);
	}

	@Override
	public void update(Video video) {
		videoService.update(video);
	}

	@Override
	public void create(Video video) {
		videoService.create(video);
	}

	@Override
	public List<Video> getAllVideoByProfileId(Long id) {
		List<Video> videos = videoService.getAllVideoByProfileId(id);
		return videos;
	}
}
