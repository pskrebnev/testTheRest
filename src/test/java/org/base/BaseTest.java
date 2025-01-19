package org.base;

import org.core.config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

  protected TestConfig testConfig;

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    testConfig = TestConfig.getInstance();
    logger.info("Test configuration initialized with base URL: {}", testConfig.getBaseUrl());
    logger.debug("Thread count: {}", testConfig.getThreadCount());
  }

  @BeforeMethod(alwaysRun = true)
  public void setTestContext(ITestResult result) {
    String testName = result.getMethod().getMethodName();
    MDC.put("testName", testName);
  }

  @AfterMethod(alwaysRun = true)
  public void clearTestContext() {
    MDC.clear();
  }
}
