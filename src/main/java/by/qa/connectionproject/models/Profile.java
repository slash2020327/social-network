package by.qa.connectionproject.models;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import by.qa.connectionproject.models.file.Music;
import by.qa.connectionproject.models.file.Video;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlRootElement(name="profile")
@XmlAccessorType(XmlAccessType.FIELD)
public class Profile extends AbstractEntity{

	private String status;
	private String login;
	private String password;
	@XmlElementWrapper(name="musicList")
	@XmlElement(name="music")
	private List<Music> musicList;
	@XmlElementWrapper(name="albums")
	@XmlElement(name="album")
	private List<Album> albums;
	@XmlElementWrapper(name="videos")
	@XmlElement(name="video")
	private List<Video> videos;
	@XmlElementWrapper(name="groups")
	@XmlElement(name="group")
	private List<Group> groups;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Music> getMusic() {
		return musicList;
	}

	public void setMusic(List<Music> musicList) {
		this.musicList = musicList;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "Profile [id=" + getId() + ", status=" + status + ", login=" + login + ", password="
				+ password + "]";
	}

}
