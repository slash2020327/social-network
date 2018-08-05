package by.qa.connectionproject.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.Group;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;
import by.qa.connectionproject.models.file.Music;
import by.qa.connectionproject.models.file.Video;

public interface IProfileDAO extends IAbstractDAO<Integer, Profile> {

	void create(User user, Profile profile) throws SQLException;
	Profile getProfileByMusicId(Integer id);
	Profile getProfileByVideoId(Integer id);
	Profile getProfileByAlbumId(Integer id);
	Profile getProfileByUserId(Integer id);
	int getPhotoNumberByProfileId(Integer id);
	int getMusicNumberByProfileId(Integer id);
	int getVideoNumberByProfileId(Integer id);
	List<Group> getAllGroupsByProfileId(Integer id);
	List<Album> getAllAlbumsByProfileId(Integer id);
	List<Video> getAllVideoByProfileId(Integer id);
	List<Music> getAllMusicByProfileId(Integer id);
}
