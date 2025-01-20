package org.tests.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class PostTests extends BaseTest {

  private static final Logger logger = LoggerFactory.getLogger(PostTests.class);
  private static final String POSTS_ENDPOINT = "/posts";

  @Test(groups = {"post",
      "critical path"}, description = "Verify POST request creates a new post with valid data")
  public void testCreatePost() {
    Map<String, Object> postData = createValidPostData();
    logger.info("Executing POST request with valid data");

    given()
        .baseUri(testConfig.getBaseUrl())
        .contentType(ContentType.JSON)
        .body(postData)
        .when()
        .post(POSTS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body("id", notNullValue())
        .body("userId", equalTo(postData.get("userId")))
        .body("title", equalTo(postData.get("title")))
        .body("body", equalTo(postData.get("body")));
  }

  @Test(groups = {"post", "negative"}
      , description = "Verify POST request with invalid content type")
  public void testCreatePostWithInvalidContentType() {
    String invalidData = "This is not JSON";
    logger.info("Executing POST request with invalid content type (not JSON)");

    given()
        .baseUri(testConfig.getBaseUrl())
        .contentType(ContentType.TEXT)
        .body(invalidData)
        .when()
        .post(POSTS_ENDPOINT)
        .then()
        .statusCode(not(HttpStatus.SC_OK));
  }

  private Map<String, Object> createValidPostData() {
    Map<String, Object> postData = new HashMap<>();
    postData.put("userId", 1);
    postData.put("title", "Test POST Title");
    postData.put("body", "Test body for testing of creation POST method");
    return postData;
  }
}
