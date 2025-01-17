package org.base;

import io.restassured.RestAssured;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.core.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

public class BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
  private static String BASE_URL;
  private static final String PROPERTY_FILE_NAME = "app.properties";

  static {
    Properties properties = new Properties();
    try (InputStream input = RestClient.class.getClassLoader()
        .getResourceAsStream(PROPERTY_FILE_NAME)) {
      if (input == null) {
        logger.error("Sorry, unable to find file '{}'", PROPERTY_FILE_NAME);
        throw new IllegalStateException("Failed to load properties");
      }
      properties.load(input);
      BASE_URL = properties.getProperty("baseUrl");
    } catch (IOException ex) {
      logger.error("IOException occurred while reading property.properties", ex);
      throw new IllegalStateException("Failed to load properties", ex);
    }
  }

  @BeforeClass
  public void setUp() {
    logger.info("Base URL: {} set up successfully", BASE_URL);
    RestAssured.baseURI = BASE_URL;
  }
}
