package org.tests.gets;

import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class GetTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(GetTests.class);

  @Test(groups = {"get", "critical path"})
  public void testGet() {
    logger.info("Testing GET /posts");
  }

  @Test(groups = {"get", "by id"})
  public void testGetById() {
    logger.info("Testing GET /posts/1");
  }
}
