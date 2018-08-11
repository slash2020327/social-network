package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.VideoDAO;
import by.qa.connectionproject.models.file.Video;
import by.qa.connectionproject.service.IVideoService;

public class VideoService implements IVideoService {

	private VideoDAO videoDAO = new VideoDAO();

	@Override
	public Video getVideoById(Long id) {
		Video video = videoDAO.getEntityById(id);
		return video;
	}

	@Override
	public List<Video> getAll() {
		List<Video> videos = videoDAO.getAll();
		return videos;
	}

	@Override
	public void delete(Long id) {
		videoDAO.delete(id);
	}

	@Override
	public void update(Video video) {
		videoDAO.update(video);
	}

	@Override
	public void create(Video video) {
		videoDAO.create(video);
	}

	@Override
	public List<Video> getAllVideoByProfileId(Long id) {
		List<Video> videos = videoDAO.getAllVideoByProfileId(id);
		return videos;
	}
}
