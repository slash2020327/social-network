package by.qa.connectionproject.parser.jacksonparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.qa.connectionproject.models.User;

public class JacksonParser {

	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	private static Logger logger = LogManager.getLogger();
	private ObjectMapper om = new ObjectMapper();

	public void writeUsersToJson(List<User> users, String filePath) {
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		om.setDateFormat(df);
		try {
			om.writeValue(new File(filePath), users);
		} catch (JsonGenerationException e) {
			logger.log(Level.ERROR, "Generation error", e);
		} catch (JsonMappingException e) {
			logger.log(Level.ERROR, "Object mapping error", e);
		} catch (IOException e) {
			logger.log(Level.ERROR, "File path is invalid", e);
		}
	}

	public List<User> readUsersFromJson(String filePath) {
		InputStream input = null;
		List<User> users = null;
		try {
			input = new FileInputStream(filePath);
			users = om.readValue(input, om.getTypeFactory().constructCollectionType(List.class, User.class));
		} catch (FileNotFoundException e) {
			logger.log(Level.ERROR, "File for parse not found", e);
		} catch (JsonParseException e) {
			logger.log(Level.ERROR, "Parse error", e);
		} catch (JsonMappingException e) {
			logger.log(Level.ERROR, "Object mapping error", e);
		} catch (IOException e) {
			logger.log(Level.ERROR, "File input stream error", e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		return users;
	}
}