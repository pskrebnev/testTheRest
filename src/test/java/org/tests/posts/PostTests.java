package org.tests.posts;

import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class PostTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(PostTests.class);

  @Test(groups = {"post", "critical path"})
  public void testPost() {
    logger.info("Testing POST /posts");
  }
}
