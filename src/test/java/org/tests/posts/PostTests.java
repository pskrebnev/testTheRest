package org.tests.posts;

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

  private Map<String, Object> createValidPostData() {
    Map<String, Object> postData = new HashMap<>();
    postData.put("userId", 1);
    postData.put("title", "Test POST Title");
    postData.put("body", "Test body for testing of creation POST method");
    return postData;
  }

  private String generateLargeString(int length) {
    StringBuilder sb = new StringBuilder(length);
    String randomStr = "Paid was hill sir high. For him precaution any advantages"
        + " dissimilar comparison few terminated projecting. Prevailed discovery"
        + " immediate objection of ye at. Repair summer one winter living feebly"
        + " pretty his. In so sense am known these since. Shortly respect ask cousins"
        + " brought add tedious nay. Expect relied do we genius is. On as around spirit"
        + " of hearts genius. Is raptures daughter branched laughter peculiar in settling.";
    for (int i = 0; i < length; i++) {
      sb.append(randomStr.charAt((int) (Math.random() * randomStr.length())));
    }
    return sb.toString();
  }
}
