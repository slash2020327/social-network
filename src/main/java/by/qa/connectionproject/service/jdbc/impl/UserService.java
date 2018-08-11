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
import by.qa.connectionproject.dao.IAbstractDAO;
import by.qa.connectionproject.dao.jdbc.impl.AlbumDAO;
import by.qa.connectionproject.dao.jdbc.impl.CityDAO;
import by.qa.connectionproject.dao.jdbc.impl.CountryDAO;
import by.qa.connectionproject.dao.jdbc.impl.DialogDAO;
import by.qa.connectionproject.dao.jdbc.impl.FriendshipDAO;
import by.qa.connectionproject.dao.jdbc.impl.GroupDAO;
import by.qa.connectionproject.dao.jdbc.impl.MusicDAO;
import by.qa.connectionproject.dao.jdbc.impl.PhotoDAO;
import by.qa.connectionproject.dao.jdbc.impl.PrivateMessageDAO;
import by.qa.connectionproject.dao.jdbc.impl.ProfileDAO;
import by.qa.connectionproject.dao.jdbc.impl.UserDAO;
import by.qa.connectionproject.dao.jdbc.impl.VideoDAO;
import by.qa.connectionproject.models.Album;
import by.qa.connectionproject.models.City;
import by.qa.connectionproject.models.Dialog;
import by.qa.connectionproject.models.PrivateMessage;
import by.qa.connectionproject.models.Profile;
import by.qa.connectionproject.models.User;
import by.qa.connectionproject.models.file.Photo;
import by.qa.connectionproject.service.IUserService;

public class UserService implements IUserService {

	private static Logger logger = LogManager.getLogger();
	private UserDAO userDAO = new UserDAO();
	private CityDAO cityDAO = new CityDAO();
	private ProfileDAO profileDAO = new ProfileDAO();
	private CountryDAO countryDAO = new CountryDAO();
	private AlbumDAO albumDAO = new AlbumDAO();
	private DialogDAO dialogDAO = new DialogDAO();
	private GroupDAO groupDAO = new GroupDAO();
	private MusicDAO musicDAO = new MusicDAO();
	private VideoDAO videoDAO = new VideoDAO();
	private PhotoDAO photoDAO = new PhotoDAO();
	private FriendshipDAO friendshipDAO = new FriendshipDAO();
	private PrivateMessageDAO messageDAO = new PrivateMessageDAO();

	@Override
	public User getUserById(Long id) {
		User user = userDAO.getEntityById(id);
		user.setCity(cityDAO.getCityByUserId(id));
		user.setProfile(profileDAO.getProfileByUserId(id));
		user.setFriendshipList(friendshipDAO.getAllFriendshipByUserId(user.getId()));
		user.setDialogues(dialogDAO.getAllDialoguesByUserId(user.getId()));
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
			profile.setGroups(groupDAO.getAllGroupsByProfileId(profile.getId()));
			profile.setMusic(musicDAO.getAllMusicByProfileId(profile.getId()));
			profile.setVideos(videoDAO.getAllVideoByProfileId(profile.getId()));
			List<Album> albums = albumDAO.getAllAlbumsByProfileId(profile.getId());
			for (Album album : albums) {
				List<Photo> photos = photoDAO.getAllPhotosByAlbumId(album.getId());
				album.setPhotos(photos);
			}
			profile.setAlbums(albums);
			List<Dialog> dialogues = dialogDAO.getAllDialoguesByUserId(user.getId());
			for (Dialog dialog : dialogues) {
				List<PrivateMessage> privateMessages = messageDAO.getAllMessagesByDialogId(dialog.getId());
				dialog.setPrivateMessages(privateMessages);
			}
			user.setDialogues(dialogues);
			user.setFriendshipList(friendshipDAO.getAllFriendshipByUserId(user.getId()));
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
			statementProfile = connection.prepareStatement(ProfileDAO.getInsertProfile(),
					Statement.RETURN_GENERATED_KEYS);
			statementProfile.setString(1, profile.getStatus());
			statementProfile.setString(2, profile.getLogin());
			statementProfile.setString(3, profile.getPassword());
			statementProfile.executeUpdate();
			statementUser = connection.prepareStatement(UserDAO.getInsertUser());
			statementUser.setString(2, user.getFirstName());
			statementUser.setString(3, user.getLastName());
			statementUser.setString(4, user.getPhoneNumber());
			statementUser.setLong(5, user.getCity().getId());
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

	@Override
	public void delete(Long id) {
		userDAO.delete(id);
	}

	@Override
	public void update(User user) {
		userDAO.update(user);
	}

	@Override
	public User getUserByDialogId(Long id) {
		User user = userDAO.getUserByDialogId(id);
		return user;
	}

	@Override
	public User getUserOneByFriendshipId(Long id) {
		User user = userDAO.getUserOneByFriendshipId(id);
		return user;
	}

	@Override
	public User getUserTwoByFriendshipId(Long id) {
		User user = userDAO.getUserTwoByFriendshipId(id);
		return user;
	}

	@Override
	public User getToUserByPrivateMessageId(Long id) {
		User user = userDAO.getToUserByPrivateMessageId(id);
		return user;
	}

	@Override
	public User getFromUserByPrivateMessageId(Long id) {
		User user = userDAO.getFromUserByPrivateMessageId(id);
		return user;
	}
}
