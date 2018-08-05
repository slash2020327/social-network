package by.qa.connectionproject.launch;

import java.util.List;
import by.qa.connectionproject.connection.ConnectionPool;
import by.qa.connectionproject.models.User;
import by.qa.connectionproject.parser.jacksonparser.JacksonParser;
import by.qa.connectionproject.parser.xmlparser.XmlParser;
import by.qa.connectionproject.service.jdbc.impl.UserService;

public class Runner {

	public static void main(String[] args) {

		ConnectionPool pool = ConnectionPool.getInstance();
		pool.init();
		UserService us = new UserService();
		List<User> users = us.getAll();
		JacksonParser jp = new JacksonParser();
		jp.writeUsersToJson(users, "data/model.json");
		jp.readUsersFromJson("data/model.json");
		XmlParser xp = new XmlParser();
		xp.writeUsersToXml(users, "data/modelxml.xml");
		xp.readUsersFromXml("data/modelxml.xml");
		
		// ArrayList<UserThread> users = new ArrayList<UserThread>();
		// for (int i = 0; i < 10; i++) {
		// UserThread user = new UserThread();
		// users.add(user);
		// }
		// for (UserThread user : users) {
		// user.start();
		// }
	}
}
