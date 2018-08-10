package by.qa.connectionproject.models;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Friendship extends AbstractEntity{

	private Long userOneId;
	private Long userTwoId;
	private FriendshipStatus status;

	public Long getUserOneId() {
		return userOneId;
	}

	public void setUserOneId(Long userOneId) {
		this.userOneId = userOneId;
	}

	public Long getUserTwoId() {
		return userTwoId;
	}

	public void setUserTwoId(Long userTwoId) {
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
