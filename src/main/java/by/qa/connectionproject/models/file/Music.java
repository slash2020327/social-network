package by.qa.connectionproject.models.file;

public class Music extends File {

	private String artistName;
	private String songName;
	private Integer profileId;

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	@Override
	public String toString() {
		return "Music [id=" + getId() +" ,artistName=" + artistName + ", songName=" + songName +  ", profileId=" + profileId + ", publicationDate=" + getPublicationDate()
				+ "]";
	}

}
