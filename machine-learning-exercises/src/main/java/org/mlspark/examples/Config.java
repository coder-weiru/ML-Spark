package org.mlspark.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

	private Properties properties = new Properties();

	private static Config instance = new Config();

	private Config() {
		String resourceName = "mlspark.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
			properties.load(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Config getInstance() {
		return instance;
	}

	public String getDataPath() {
		return (String) properties.get("data.path");
	}
}
