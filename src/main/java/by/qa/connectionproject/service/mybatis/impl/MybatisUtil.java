package by.qa.connectionproject.service.mybatis.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MybatisUtil {

	private static SqlSessionFactory sqlSessionFactory;
	private static Logger logger = LogManager.getLogger();
	public final static String PATH_TO_CONFIG = "mybatis-config.xml";

	private MybatisUtil() {
	}

	static {
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(PATH_TO_CONFIG);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			logger.log(Level.ERROR, "Mybatis config file was not found", e);
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}
