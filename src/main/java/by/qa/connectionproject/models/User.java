package by.qa.connectionproject.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User extends AbstractEntity {

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private City city;
	private Profile profile;
	@XmlElementWrapper(name="friendshipList")
	@XmlElement(name="friendship")
	private List<Friendship> friendshipList;
	@XmlElementWrapper(name="dialogues")
	@XmlElement(name="dialog")
	private List<Dialog> dialogues;

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<Dialog> getDialogues() {
		return dialogues;
	}

	public void setDialogues(List<Dialog> dialogues) {
		this.dialogues = dialogues;
	}

	public List<Friendship> getFriendshipList() {
		return friendshipList;
	}

	public void setFriendshipList(List<Friendship> friendshipList) {
		this.friendshipList = friendshipList;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return "User [id=" + getId() + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + ", cityName=" + city.getCityName() + ", profileId=" + profile.getId() + "]";
	}

}
