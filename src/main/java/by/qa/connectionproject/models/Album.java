package by.qa.connectionproject.models;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import by.qa.connectionproject.models.file.Photo;

@XmlRootElement(name="album")
@XmlAccessorType(XmlAccessType.FIELD)
public class Album extends AbstractEntity{

	private String albumName;
	private Integer profileId;
	@XmlElementWrapper(name="photos")
	@XmlElement(name="photo")
	private List<Photo> photos;
	
	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	@Override
	public String toString() {
		return "Album [id=" + getId() + ", albumName=" + albumName + ", profileId=" + profileId + "]";
	}

}
