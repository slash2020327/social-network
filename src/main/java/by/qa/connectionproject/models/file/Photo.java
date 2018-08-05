package by.qa.connectionproject.models.file;

public class Photo extends File {

	private Integer albumId;

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	@Override
	public String toString() {
		return "Photo [id=" + getId() + ", publicationDate=" + getPublicationDate() + ", albumId=" + albumId + "]";
	}

}
