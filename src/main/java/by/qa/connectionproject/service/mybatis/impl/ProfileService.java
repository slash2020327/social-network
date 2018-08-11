package by.qa.connectionproject.service.mybatis.impl;

import java.util.List;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.service.IProfileService;

public class ProfileService implements IProfileService {

	private IProfileService profileService = MybatisUtil.getSqlSessionFactory().openSession().getMapper(IProfileService.class);

	@Override
	public Profile getProfileById(Long id) {
		Profile profile = profileService.getProfileById(id);
		return profile;
	}

	@Override
	public List<Profile> getAll() {
		List<Profile> profiles = profileService.getAll();
		return profiles;
	}

	@Override
	public void delete(Long id) {
		profileService.delete(id);
	}

	@Override
	public void update(Profile profile) {
		profileService.update(profile);
	}

	@Override
	public Profile getProfileByMusicId(Long id) {
		Profile profile = profileService.getProfileByMusicId(id);
		return profile;
	}

	@Override
	public Profile getProfileByVideoId(Long id) {
		Profile profile = profileService.getProfileByVideoId(id);
		return profile;
	}

	@Override
	public Profile getProfileByAlbumId(Long id) {
		Profile profile = profileService.getProfileByAlbumId(id);
		return profile;
	}

	@Override
	public Profile getProfileByUserId(Long id) {
		Profile profile = profileService.getProfileByUserId(id);
		return profile;
	}

	@Override
	public List<Profile> getAllProfilesByGroupId(Long id) {
		List<Profile> profiles = profileService.getAllProfilesByGroupId(id);
		return profiles;
	}
}
