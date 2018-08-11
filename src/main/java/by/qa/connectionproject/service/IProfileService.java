package by.qa.connectionproject.service;

import java.util.List;
import by.qa.connectionproject.models.Profile;

public interface IProfileService  {

	List<Profile> getAll();

	Profile getProfileById(Long id);

	void delete(Long id);

	void update(Profile profile);
	
	Profile getProfileByMusicId(Long id);

	Profile getProfileByVideoId(Long id);

	Profile getProfileByAlbumId(Long id);

	Profile getProfileByUserId(Long id);
	
	List<Profile> getAllProfilesByGroupId(Long id);
}
