package by.qa.connectionproject.dao;

import java.util.List;
import by.qa.connectionproject.models.Profile;

public interface IProfileDAO extends IAbstractDAO<Long, Profile> {

	List<Profile> getAll();

	Profile getEntityById(Long id);

	void delete(Long id);

	void update(Profile profile);
	
	Profile getProfileByMusicId(Long id);

	Profile getProfileByVideoId(Long id);

	Profile getProfileByAlbumId(Long id);

	Profile getProfileByUserId(Long id);
	
	List<Profile> getAllProfilesByGroupId(Long id);
}
