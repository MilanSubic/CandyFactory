package org.unibl.etf.rest.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionFactoryUtil {

	public static Connection createConnection() throws IOException, TimeoutException {
		Properties prop;
		prop = new Properties();
		prop.load(new FileInputStream(new File("clientApp.properties")));
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(prop.getProperty("LOCALHOST"));
		factory.setUsername(prop.getProperty("USERNAME"));
		factory.setPassword(prop.getProperty("PASSWORD") );
		return factory.newConnection();
	}
}
