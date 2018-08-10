package by.qa.connectionproject.models;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import by.qa.connectionproject.parser.xmlparser.DateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class PrivateMessage extends AbstractEntity {

	private Long fromUserId;
	private Long toUserId;
	private String messageText;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date dateSend;
	private Integer dialogId;

	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public Date getDateSend() {
		return dateSend;
	}

	public void setDateSend(Date dateSend) {
		this.dateSend = dateSend;
	}

	public Integer getDialogId() {
		return dialogId;
	}

	public void setDialogId(Integer dialogId) {
		this.dialogId = dialogId;
	}

	@Override
	public String toString() {
		return "PrivateMessage [id=" + getId() + ", fromUserId=" + fromUserId + ", toUserId=" + toUserId
				+ ", messageText=" + messageText + ", dateSend=" + dateSend + ", dialogId=" + dialogId + "]";
	}

}
