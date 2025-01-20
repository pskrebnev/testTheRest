package org.tests.patch;

import static io.restassured.RestAssured.given;
import static org.core.util.Utils.getRandomId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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

public class PatchTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(PatchTests.class);
  private static final String PATCH_ENDPOINT = "/posts";
  private static final String PATCH_BY_ID_ENDPOINT = PATCH_ENDPOINT + "/{id}";

  @Test(groups = {"patch", "critical path"}, description = "Verify PATCH request updates"
      + " specific field of an existed post")
  public void testPartialUpdatePost() {
    int postId = getRandomId();
    logger.info("Executing PATCH request to update post with ID: {}", postId);
    Map<String, Object> patchData = new HashMap<>();
    patchData.put("title", "Updated title via PATCH");

    given()
        .baseUri(testConfig.getBaseUrl())
        .contentType(ContentType.JSON)
        .pathParam("id", postId)
        .body(patchData)
        .when()
        .patch(PATCH_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", equalTo(postId))
        .body("title", equalTo(patchData.get("title")))
        .body("userId", notNullValue())
        .body("body", notNullValue());
  }

  @Test(groups = {"patch", "critical path"}, description = "Verify PATCH request updates with"
      + " multiple fields of an existed post")
  public void testMultipleFieldsUpdatePost() {
    int postId = getRandomId();

    Map<String, Object> patchData = new HashMap<>();
    patchData.put("title", "Updated title via PATCH");
    patchData.put("body", "Updated body via PATCH");

    logger.info("Executing PATCH request to update post with ID (multiple fields): {}", postId);

    given()
        .baseUri(testConfig.getBaseUrl())
        .contentType(ContentType.JSON)
        .pathParam("id", postId)
        .body(patchData)
        .when()
        .patch(PATCH_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", equalTo(postId))
        .body("title", equalTo(patchData.get("title")))
        .body("body", equalTo(patchData.get("body")))
        .body("userId", notNullValue());
  }

  @Test(groups = {"patch", "negative"}, description = "Verify PATCH request with invalid"
      + " ID returns server error")
  public void testUpdateNonExistedPost() {
    int nonExistedId = 1999;
    Map<String, Object> patchData = new HashMap<>();
    patchData.put("title", "Updated title via PATCH for non-existed ID: " + nonExistedId);

    logger.info("Executing PATCH request to update post with non-existed ID: {}", nonExistedId);

    given()
        .baseUri(testConfig.getBaseUrl())
        .contentType(ContentType.JSON)
        .pathParam("id", nonExistedId)
        .body(patchData)
        .when()
        .patch(PATCH_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK) // In this case the server returns 200 OK
        .body("id", nullValue()) // but this id is not found in the response
        .body("title", equalTo(patchData.get("title")));
  }

  @Test(groups = {"patch", "negative"}, description = "Verify PATCH request with invalid"
      + " JSON returns parsing error")
  public void testUpdatePostWithInvalidJson() {
    int postId = getRandomId();
    String invalidData = "This is not JSON";
    logger.info("Executing PATCH request with invalid JSON for ID: {}", postId);

    Response response = given()
        .baseUri(testConfig.getBaseUrl())
        .header("Content-Type", "application/json")
        .pathParam("id", postId)
        .body(invalidData)
        .when()
        .patch(PATCH_BY_ID_ENDPOINT);

    String responseBody = response.getBody().asString();
    logger.info("Response body: {}", responseBody);
    logger.info("Response status code: {}", response.getStatusCode());

    Assert.assertTrue(responseBody.contains("SyntaxError"),
        "Response should contain JSON 'SyntaxError'");
    Assert.assertTrue(responseBody.contains("Unexpected token"),
        "Response should contain 'Unexpected token' error");
    Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_INTERNAL_SERVER_ERROR,
        "Response status code should be 500");
  }
}
