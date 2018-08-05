package by.qa.connectionproject.models.file;

public class Video extends File {

	private String name;
	private Integer profileId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	@Override
	public String toString() {
		return "Video [id=" + getId() + ", name=" + name + ", publicationDate=" + getPublicationDate() + ", profileId="
				+ profileId + "]";
	}

}
