package by.qa.connectionproject.dao.jdbc;

import java.util.List;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.Group;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.file.Music;
import by.qa.connectionproject.models.file.Video;

public interface IProfileDAO extends IAbstractDAO<Integer, Profile> {

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
