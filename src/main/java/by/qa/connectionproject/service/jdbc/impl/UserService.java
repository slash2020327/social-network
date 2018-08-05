package by.qa.connectionproject.service.jdbc.impl;

import by.qa.connectionproject.dao.jdbc.impl.AlbumDAO;
import by.qa.connectionproject.dao.jdbc.impl.CityDAO;
import by.qa.connectionproject.dao.jdbc.impl.CountryDAO;
import by.qa.connectionproject.dao.jdbc.impl.DialogDAO;
import by.qa.connectionproject.dao.jdbc.impl.ProfileDAO;
import by.qa.connectionproject.dao.jdbc.impl.UserDAO;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Dialog;
import by.qa.connectionproject.models.PrivateMessage;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;
import by.qa.connectionproject.models.file.Photo;
import java.util.ArrayList;
import java.util.List;

public class UserService {

	private UserDAO userDAO = new UserDAO();
	private CityDAO cityDAO = new CityDAO();
	private ProfileDAO profileDAO = new ProfileDAO();
	private CountryDAO countryDAO = new CountryDAO();
	private AlbumDAO albumDAO = new AlbumDAO();
	private DialogDAO dialogDAO = new DialogDAO();

	public User getUserById(Integer id) {
		User user = userDAO.getEntityById(id);
		user.setCity(cityDAO.getCityByUserId(id));
		user.setProfile(profileDAO.getProfileByUserId(id));
		return user;
	}

	public List<User> getAll() {
		List<User> users = new ArrayList<>();
		users = userDAO.getAll();
		for (User user : users) {
			user.setProfile(profileDAO.getProfileByUserId(user.getId()));
			user.setCity(cityDAO.getCityByUserId(user.getId()));
			City city = user.getCity();
			city.setCountry(countryDAO.getCountryByCityId(city.getId()));
			Profile profile = user.getProfile();
			profile.setGroups(profileDAO.getAllGroupsByProfileId(profile.getId()));
			profile.setMusic(profileDAO.getAllMusicByProfileId(profile.getId()));
			profile.setVideos(profileDAO.getAllVideoByProfileId(profile.getId()));
			List<Album> albums = profileDAO.getAllAlbumsByProfileId(profile.getId());
			for (Album album : albums) {
				List<Photo> photos = albumDAO.getAllPhotosByAlbumId(album.getId());
				album.setPhotos(photos);
			}
			profile.setAlbums(albums);
			List<Dialog> dialogues = userDAO.getAllDialoguesByUserId(user.getId());
			for (Dialog dialog : dialogues) {
				List<PrivateMessage> privateMessages = dialogDAO.getAllMessagesByDialogId(dialog.getId());
				dialog.setPrivateMessages(privateMessages);
			}
			user.setDialogues(dialogues);
			user.setFriendshipList(userDAO.getAllFriendshipByUserId(user.getId()));
		}
		return users;
	}
}
