package by.qa.connectionproject.models.file;

public class Photo extends File {

	private Long albumId;

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	@Override
	public String toString() {
		return "Photo [id=" + getId() + ", publicationDate=" + getPublicationDate() + ", albumId=" + albumId + "]";
	}

}
