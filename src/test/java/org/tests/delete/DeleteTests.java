package org.tests.delete;

import static io.restassured.RestAssured.given;
import static org.core.util.Utils.getRandomId;
import static org.hamcrest.Matchers.equalTo;

import org.apache.http.HttpStatus;
import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class DeleteTests extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(DeleteTests.class);
  private static final String DELETE_ENDPOINT = "/posts";
  private static final String DELETE_BY_ID_ENDPOINT = DELETE_ENDPOINT + "/{id}";

  @Test(groups = {"delete",
      "critical path"}, description = "Verify DELETE request removes an existing pots")
  public void testDeletePost() {
    int postId = getRandomId();
    logger.info("Verify DELETE request removes a random existing post: {}", postId);

    given()
        .baseUri(testConfig.getBaseUrl())
        .pathParam("id", postId)
        .when()
        .delete(DELETE_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(equalTo("{}")); // Empty response body
  }

  @Test(groups = {"delete"}, description = "Verify DELETE request with non-existed ID returns 404")
  public void testDeleteNonExistedPost() {
    int nonExistedId = 1999;
    logger.info("Verify DELETE request with non-existed post ID: {} returns 404", nonExistedId);

    given()
        .baseUri(testConfig.getBaseUrl())
        .pathParam("id", nonExistedId)
        .when()
        .get(DELETE_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }
}
