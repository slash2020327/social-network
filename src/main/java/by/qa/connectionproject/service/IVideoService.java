package by.qa.connectionproject.service;

import java.util.List;

import by.qa.connectionproject.models.file.Video;

public interface IVideoService {

	List<Video> getAll();

	Video getVideoById(Long id);

	void delete(Long id);

	void update(Video video);
	
	void create(Video video);
	
	List<Video> getAllVideoByProfileId(Long id);
}
