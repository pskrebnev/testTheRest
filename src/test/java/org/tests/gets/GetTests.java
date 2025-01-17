package org.tests.gets;

import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class GetTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(GetTests.class);

  @Test
  public void testGetUsers() {
    logger.info("Testing GET /posts");
  }
}
