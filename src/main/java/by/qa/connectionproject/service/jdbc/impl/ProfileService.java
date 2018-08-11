package by.qa.connectionproject.service.jdbc.impl;

import java.util.List;
import by.qa.connectionproject.dao.jdbc.impl.AlbumDAO;
import by.qa.connectionproject.dao.jdbc.impl.GroupDAO;
import by.qa.connectionproject.dao.jdbc.impl.MusicDAO;
import by.qa.connectionproject.dao.jdbc.impl.PhotoDAO;
import by.qa.connectionproject.dao.jdbc.impl.ProfileDAO;
import by.qa.connectionproject.dao.jdbc.impl.VideoDAO;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.file.Photo;
import by.qa.connectionproject.service.IProfileService;

public class ProfileService implements IProfileService {

	private ProfileDAO profileDAO = new ProfileDAO();
	private GroupDAO groupDAO = new GroupDAO();
	private MusicDAO musicDAO = new MusicDAO();
	private VideoDAO videoDAO = new VideoDAO();
	private PhotoDAO photoDAO = new PhotoDAO();
	private AlbumDAO albumDAO = new AlbumDAO();

	@Override
	public Profile getProfileById(Long id) {
		Profile profile = profileDAO.getEntityById(id);
		profile.setGroups(groupDAO.getAllGroupsByProfileId(profile.getId()));
		profile.setMusic(musicDAO.getAllMusicByProfileId(profile.getId()));
		profile.setVideos(videoDAO.getAllVideoByProfileId(profile.getId()));
		List<Album> albums = albumDAO.getAllAlbumsByProfileId(profile.getId());
		for (Album album : albums) {
			List<Photo> photos = photoDAO.getAllPhotosByAlbumId(album.getId());
			album.setPhotos(photos);
		}
		profile.setAlbums(albums);
		return profile;
	}

	@Override
	public List<Profile> getAll() {
		List<Profile> profiles = profileDAO.getAll();
		for (Profile profile : profiles) {
			profile.setGroups(groupDAO.getAllGroupsByProfileId(profile.getId()));
			profile.setMusic(musicDAO.getAllMusicByProfileId(profile.getId()));
			profile.setVideos(videoDAO.getAllVideoByProfileId(profile.getId()));
			List<Album> albums = albumDAO.getAllAlbumsByProfileId(profile.getId());
			for (Album album : albums) {
				List<Photo> photos = photoDAO.getAllPhotosByAlbumId(album.getId());
				album.setPhotos(photos);
			}
			profile.setAlbums(albums);
		}
		return profiles;
	}

	@Override
	public void delete(Long id) {
		profileDAO.delete(id);
	}

	@Override
	public void update(Profile profile) {
		profileDAO.update(profile);
	}

	@Override
	public Profile getProfileByMusicId(Long id) {
		Profile profile = profileDAO.getProfileByMusicId(id);
		return profile;
	}

	@Override
	public Profile getProfileByVideoId(Long id) {
		Profile profile = profileDAO.getProfileByVideoId(id);
		return profile;
	}

	@Override
	public Profile getProfileByAlbumId(Long id) {
		Profile profile = profileDAO.getProfileByAlbumId(id);
		return profile;
	}

	@Override
	public Profile getProfileByUserId(Long id) {
		Profile profile = profileDAO.getProfileByUserId(id);
		return profile;
	}

	@Override
	public List<Profile> getAllProfilesByGroupId(Long id) {
		List<Profile> profiles = profileDAO.getAllProfilesByGroupId(id);
		return profiles;
	}
}
