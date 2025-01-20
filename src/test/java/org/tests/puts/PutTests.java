package org.tests.puts;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private Map<String, Object> createValidUpdateData() {
    Map<String, Object> updateData = new HashMap<>();
    updateData.put("userId", 1);
    updateData.put("title", "Updated POST title");
    updateData.put("body", "Updated body content for testing PUT request");
    return updateData;
  }
}
