package by.qa.connectionproject.models;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Friendship extends AbstractEntity{

	private Integer userOneId;
	private Integer userTwoId;
	private FriendshipStatus status;

	public Integer getUserOneId() {
		return userOneId;
	}

	public void setUserOneId(Integer userOneId) {
		this.userOneId = userOneId;
	}

	public Integer getUserTwoId() {
		return userTwoId;
	}

	public void setUserTwoId(Integer userTwoId) {
		this.userTwoId = userTwoId;
	}

	public FriendshipStatus getStatus() {
		return status;
	}

	public void setStatus(FriendshipStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Friendship [id=" + getId() + ", userOneId=" + userOneId + ", userTwoId=" + userTwoId + ", status=" + status
				+ "]";
	}

}
