package by.qa.connectionproject.models.file;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import by.qa.connectionproject.models.AbstractEntity;
import by.qa.connectionproject.parser.xmlparser.DateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class File extends AbstractEntity{

	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date publicationDate;

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

}
