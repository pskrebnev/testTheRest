package org.tests.puts;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

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

public class PutTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(PutTests.class);
  private static final String PUTS_ENDPOINT = "/posts";
  private static final String PUTS_BY_ID_ENDPOINT = PUTS_ENDPOINT + "/{id}";

  @Test(groups = {"put", "critical path"}, description = "Verify PUT request updates"
      + " a post with valid data")
  public void testUpdatePost() {
    int postId = 1;
    Map<String, Object> updatedData = createValidUpdateData();
    logger.info("Executing PUT request to update post with ID: {}", postId);

    given()
        .baseUri(testConfig.getBaseUrl())
        .contentType(ContentType.JSON)
        .pathParam("id", postId)
        .body(updatedData)
        .when()
        .put(PUTS_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", equalTo(postId))
        .body("userId", equalTo(updatedData.get("userId")))
        .body("title", equalTo(updatedData.get("title")))
        .body("body", equalTo(updatedData.get("body")));
  }

  @Test(groups = {"put", "negative"}, description = "Verify PUT request with invalid"
      + " ID returns server error")
  public void testUpdateNonExistedPost() {
    int nonExistedId = 1999;
    Map<String, Object> updatedData = createValidUpdateData();
    logger.info("Executing PUT request to update post with non-existed ID: {}", nonExistedId);

    given()
        .baseUri(testConfig.getBaseUrl())
        .contentType(ContentType.JSON)
        .pathParam("id", nonExistedId)
        .body(updatedData)
        .when()
        .put(PUTS_BY_ID_ENDPOINT)
        .then()
        .statusCode(not(HttpStatus.SC_OK));
  }

  @Test(groups = {"put", "negative"}, description = "Verify PUT request with invalid"
      + " JSON returns parsing error")
  public void testUpdatePostWithInvalidJson() {
    int postId = (int) (Math.random() * 100) + 1;
    logger.info("Executing PUT request with invalid JSON for ID: {}", postId);

    String invalidData = "This is not JSON";
    Response response = given()
        .baseUri(testConfig.getBaseUrl())
        .header("Content-Type", "application/json")
        .pathParam("id", postId)
        .body(invalidData)
//        .log().all()
        .when()
        .put(PUTS_BY_ID_ENDPOINT)
        .then()
//        .log().all()
        .extract()
        .response();

    String responseBody = response.getBody().asString();
//    logger.info("Response status code: {}", response.getStatusCode());
//    logger.info("Response body: {}", responseBody);

    // Verify response
    Assert.assertTrue(responseBody.contains("SyntaxError")
        , "Response body should contain JSON syntax error");
    Assert.assertTrue(responseBody.contains("Unexpected token")
        , "Response body should contain unexpected token error");
    Assert.assertTrue(responseBody.contains("JSON.parse")
        , "Response body should indicate JSON parsing failure");
  }

  private Map<String, Object> createValidUpdateData() {
    Map<String, Object> updateData = new HashMap<>();
    updateData.put("userId", 1);
    updateData.put("title", "Updated POST title");
    updateData.put("body", "Updated body content for testing PUT request");
    return updateData;
  }
}
