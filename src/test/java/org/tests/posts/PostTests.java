package org.tests.posts;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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

  // TODO: Test failed
  @Test(groups = {"post",
      "negative"}, description = "Verify POST request fails with missing required fields")
  public void testCreatePostWithMissingFields() {
    Map<String, Object> invalidData = new HashMap<>();
    invalidData.put("title", "Test invalid Title");
    logger.info("Executing POST request with missing required fields");

    Response response = given()
        .baseUri(testConfig.getBaseUrl())
        .contentType(ContentType.JSON)
        .body(invalidData)
        .when()
        .post(POSTS_ENDPOINT)
        .then()
        .statusCode(not(HttpStatus.SC_OK))
        .extract().response();

    Assert.assertTrue(response.statusCode() >= 400,
        "The error status code is expected for invalid request");
  }

  private Map<String, Object> createValidPostData() {
    Map<String, Object> postData = new HashMap<>();
    postData.put("userId", 1);
    postData.put("title", "Test POST Title");
    postData.put("body", "Test body for testing of creation POST method");
    return postData;
  }
}
