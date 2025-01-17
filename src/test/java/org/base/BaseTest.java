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
  private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
  private static final String PROPERTY_FILE_NAME = "app.properties";
  private static final String ERR_MESSAGE = "Failed to load properties";
  private static String BASE_URL;

  static {
    Properties properties = new Properties();
    try (InputStream input = RestClient.class.getClassLoader()
        .getResourceAsStream(PROPERTY_FILE_NAME)) {
      if (input == null) {
        logger.error("Unable to find file '{}'", PROPERTY_FILE_NAME);
        throw new IllegalStateException(ERR_MESSAGE);
      }
      properties.load(input);
      BASE_URL = properties.getProperty("baseUrl");
    } catch (IOException ex) {
      logger.error("IOException occurred while reading {}", PROPERTY_FILE_NAME, ex);
      throw new IllegalStateException(ERR_MESSAGE, ex);
    }
  }

  @BeforeClass
  public void setUp() {
    logger.info("Base URL: {} set up successfully", BASE_URL);
    RestAssured.baseURI = BASE_URL;
  }
}
