package org.tests.gets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.apache.http.HttpStatus;
import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GetTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(GetTests.class);
  private static final String POSTS_ENDPOINT = "/posts";
  private static final String POST_BY_ID_ENDPOINT = POSTS_ENDPOINT + "/{id}";

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

  @Test(groups = {"get"}, dataProvider = "postIds")
  public void testGetPostById(int postId) {
    logger.info("Testing GET /posts/{}", postId);
    logger.info("Making request to: {}{}", testConfig.getBaseUrl(), POST_BY_ID_ENDPOINT);

    given()
        .baseUri(testConfig.getBaseUrl())
        .pathParam("id", postId)
        .when()
        .get(POST_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", equalTo(postId))
        .body("userId", notNullValue())
        .body("title", not(emptyString()))
        .body("body", not(emptyString()));
  }

  @Test(groups = {"get", "negative"})
  public void testGetPostByInvalidId() {
    int invalidId = 999;
    logger.info("Testing GET /posts/{} with invalid ID", invalidId);
    logger.info("Making request to: {}{}", testConfig.getBaseUrl(), POST_BY_ID_ENDPOINT);

    given()
        .baseUri(testConfig.getBaseUrl())
        .pathParam("id", invalidId)
        .when()
        .get(POST_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  // Data provider divided to the following: first, middle, last
  // All IDs are valid
  @DataProvider(name = "postIds")
  public Object[][] postIds() {
    return new Object[][] {
        {1},
        {50},
        {100}
    };
  }
}
