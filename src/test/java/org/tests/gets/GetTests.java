package org.tests.gets;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class GetTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(GetTests.class);
  private static final String POSTS_ENDPOINT = "/posts";

  @Test(groups = {"get", "critical path"})
  public void testGetPost() {
    logger.info("Testing GET /posts");
    logger.info("Making request to: {}{}", testConfig.getBaseUrl(), POSTS_ENDPOINT);

    given()
        .baseUri(testConfig.getBaseUrl())
        .when()
        .get(POSTS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK);
  }
}
