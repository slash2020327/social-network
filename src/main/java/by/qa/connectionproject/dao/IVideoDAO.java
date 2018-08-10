package by.qa.connectionproject.dao;

import java.util.List;

import by.qa.connectionproject.models.file.Video;

public interface IVideoDAO extends IAbstractDAO<Long, Video> {

	List<Video> getAll();

	Video getEntityById(Long id);

	void delete(Long id);

	void update(Video video);
	
	void create(Video video);
	
	List<Video> getAllVideoByProfileId(Long id);
}
