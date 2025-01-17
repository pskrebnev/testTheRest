package org.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class TestConfig {
  private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);
  private static final String PROPERTY_FILE_NAME = "app.properties";
  private static final String ERR_MESSAGE = "Failed to load properties";

  // Default values as constants
  private static final String DEFAULT_BASE_URL = "https://jsonplaceholder.typicode.com";
  private static final int DEFAULT_THREAD_COUNT = 4;

  private static volatile TestConfig instance;
  private final ConfigProperties configProperties;

  private TestConfig() {
    Properties properties = loadProperties();
    this.configProperties = ConfigProperties.builder()
        .properties(properties)
        .baseUrl(getProperty(properties, "api.baseUrl", DEFAULT_BASE_URL))
        .threadCount(getIntProperty(properties, "test.thread.count", DEFAULT_THREAD_COUNT))
        .build();
  }

  // Only one 'test config' instance
  public static TestConfig getInstance() {
    if (instance == null) {
      synchronized (TestConfig.class) {
        if (instance == null) {
          instance = new TestConfig();
        }
      }
    }
    return instance;
  }

  public String getBaseUrl() {
    return configProperties.getBaseUrl();
  }

  public int getThreadCount() {
    return configProperties.getThreadCount();
  }

  private Properties loadProperties() {
    Properties props = new Properties();
    try (InputStream input = getClass().getClassLoader()
        .getResourceAsStream(PROPERTY_FILE_NAME)) {
      if (input == null) {
        logger.warn("{}. {} not found, using defaults", ERR_MESSAGE, PROPERTY_FILE_NAME);
        return props;
      }
      props.load(input);
    } catch (IOException ex) {
      logger.error(ERR_MESSAGE, ex);
    }
    return props;
  }

  // Helper methods for property parsing
  private String getProperty(Properties props, String key, String defaultValue) {
    String value = props.getProperty(key);
    if (value == null) {
      logger.debug("Property '{}' not found, using default value: {}", key, defaultValue);
      return defaultValue;
    }
    return value;
  }

  private int getIntProperty(Properties props, String key, int defaultValue) {
    try {
      return Integer.parseInt(getProperty(props, key, String.valueOf(defaultValue)));
    } catch (NumberFormatException e) {
      logger.warn("Failed to parse integer property '{}', using default value: {}", key,
          defaultValue);
      return defaultValue;
    }
  }

  @Getter
  @lombok.Builder
  private static class ConfigProperties {
    private final Properties properties;
    private final String baseUrl;
    private final int threadCount;
  }
}
