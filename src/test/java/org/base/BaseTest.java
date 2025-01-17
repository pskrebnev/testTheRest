package org.base;

import org.core.config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

public class BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

  protected TestConfig testConfig;

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    testConfig = TestConfig.getInstance();
    logger.info("Test configuration initialized with base URL: {}", testConfig.getBaseUrl());
    logger.info("Thread count: {}", testConfig.getThreadCount());
  }
}
