package by.qa.connectionproject.parser.xmlparser;

import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.qa.connectionproject.models.User;
import by.qa.connectionproject.models.UserList;

public class XmlParser {

	private static Logger logger = LogManager.getLogger();

	public void writeUsersToXml(List<User> users, String filePath) {
		UserList userList = new UserList();
		userList.setUsers(users);
		JAXBContext context = null;
		Marshaller marshaller = null;
		try {
			context = JAXBContext.newInstance(UserList.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(userList, new File(filePath));
		} catch (JAXBException e) {
			logger.log(Level.ERROR, "Parse error", e);
		}
	}

	public List<User> readUsersFromXml(String filePath) {
		JAXBContext context = null;
		List<User> users = null;
		try {
			File file = new File("data/modelxml.xml");
			context = JAXBContext.newInstance(UserList.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			UserList userList = (UserList) unmarshaller.unmarshal(file);
			users = userList.getUsers();
		} catch (JAXBException e) {
			logger.log(Level.ERROR, "Parse error", e);
		}
		return users;
	}
}
