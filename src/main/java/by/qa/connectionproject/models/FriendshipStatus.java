package by.qa.connectionproject.models;

public enum FriendshipStatus {

	PENDING("Pending"),ACCEPTED("Accepted"),DECLINED("Declined"),BLOCKED("Blocked");
	
	private String status;
	
	FriendshipStatus (String status) {
		this.status=status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
