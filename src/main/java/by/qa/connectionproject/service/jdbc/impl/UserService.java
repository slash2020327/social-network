package by.qa.connectionproject.service.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.qa.connectionproject.connection.ConnectionPool;
import by.qa.connectionproject.dao.jdbc.IAbstractDAO;
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
import by.qa.connectionproject.service.jdbc.IUserService;

public class UserService implements IUserService {

	private static Logger logger = LogManager.getLogger();
	private UserDAO userDAO = new UserDAO();
	private CityDAO cityDAO = new CityDAO();
	private ProfileDAO profileDAO = new ProfileDAO();
	private CountryDAO countryDAO = new CountryDAO();
	private AlbumDAO albumDAO = new AlbumDAO();
	private DialogDAO dialogDAO = new DialogDAO();

	@Override
	public User getUserById(Integer id) {
		User user = userDAO.getEntityById(id);
		user.setCity(cityDAO.getCityByUserId(id));
		user.setProfile(profileDAO.getProfileByUserId(id));
		return user;
	}

	@Override
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
	
	@Override
	public void create(User user, Profile profile) {
		Connection connection = null;
		PreparedStatement statementUser = null;
		PreparedStatement statementProfile = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			connection.setAutoCommit(false);
			statementProfile = connection.prepareStatement(ProfileDAO.getInsertProfile(), Statement.RETURN_GENERATED_KEYS);
			statementProfile.setString(1, profile.getStatus());
			statementProfile.setString(2, profile.getLogin());
			statementProfile.setString(3, profile.getPassword());
			statementProfile.executeUpdate();
			statementUser = connection.prepareStatement(UserDAO.getInsertUser());
			statementUser.setString(2, user.getFirstName());
			statementUser.setString(3, user.getLastName());
			statementUser.setString(4, user.getPhoneNumber());
			statementUser.setInt(5, user.getCity().getId());
			resultSet = statementProfile.getGeneratedKeys();
			if (resultSet.next()) {
				statementUser.setInt(1, resultSet.getInt(1));
			}
			statementUser.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Creation error", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Connection rollback error", e);
			}
		} finally {
			IAbstractDAO.setTrueAutoCommit(connection);
			IAbstractDAO.closeResultSet(resultSet);
			IAbstractDAO.closePreparedStatement(statementProfile);
			IAbstractDAO.closePreparedStatement(statementUser);
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}
}
