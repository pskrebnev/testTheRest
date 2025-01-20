package org.tests.patch;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class PatchTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(PatchTests.class);
  private static final String PATCH_ENDPOINT = "/posts";
  private static final String PATCH_BY_ID_ENDPOINT = PATCH_ENDPOINT + "/{id}";

  @Test(groups = {"patch", "critical path"}, description = "Verify PATCH request updates"
      + " specific field of an existed post")
  public void testPartialUpdatePost() {
    int postId = (int) (Math.random() * 100) + 1;
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
    int postId = (int) (Math.random() * 100) + 1;

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
}
