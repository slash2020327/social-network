package by.qa.connectionproject.models;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import by.qa.connectionproject.parser.xmlparser.DateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Dialog extends AbstractEntity {

	private Integer dialogOwnerId;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date creationDate;
	private List<PrivateMessage> privateMessages;

	public Integer getDialogOwnerId() {
		return dialogOwnerId;
	}

	public void setDialogOwnerId(Integer dialogOwnerId) {
		this.dialogOwnerId = dialogOwnerId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<PrivateMessage> getPrivateMessages() {
		return privateMessages;
	}

	public void setPrivateMessages(List<PrivateMessage> privateMessages) {
		this.privateMessages = privateMessages;
	}

	@Override
	public String toString() {
		return "Dialog [dialogId=" + getId() + " ,creationDate=" + creationDate + " ,dialogOwnerId=" + dialogOwnerId + "]";
	}

}
