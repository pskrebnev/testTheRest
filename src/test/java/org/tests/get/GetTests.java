package org.tests.get;

import static io.restassured.RestAssured.given;
import static org.core.util.Utils.getRandomId;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.response.Response;
import java.util.List;
import org.apache.http.HttpStatus;
import org.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GetTests extends BaseTest {

  private static final Logger logger = LoggerFactory.getLogger(GetTests.class);

  private static final String POSTS_ENDPOINT = "/posts";
  private static final String POST_BY_ID_ENDPOINT = POSTS_ENDPOINT + "/{id}";
  private static final String POST_COMMENTS_ENDPOINT = POSTS_ENDPOINT + "/{postId}/comments";

  @Test(groups = {"get", "critical path"}
      , description = "Verify GET request returns 200")
  public void testGetPost() {
    logger.info("Executing GET request");

    given()
        .baseUri(testConfig.getBaseUrl())
        .when()
        .get(POSTS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK);
  }

  @Test(groups = {"get"}, dataProvider = "postIds"
      , description = "Verify GET request has 'id', 'userId', 'title', 'body fields")
  public void testGetPostById(int postId) {
    logger.info("Executing GET with ID {}", postId);

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

  @Test(groups = {"get", "negative"}
      , description = "Verify invalid id returns 'not found' error")
  public void testGetPostByInvalidId() {
    int invalidId = 1999;
    logger.info("Executing GET with invalid ID: {}", invalidId);

    given()
        .baseUri(testConfig.getBaseUrl())
        .pathParam("id", invalidId)
        .when()
        .get(POST_BY_ID_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test(groups = {"get", "comments", "critical path"}, dataProvider = "postIds"
      , description = "Verify every comment has the following fields:"
      + " 'postId', 'id', 'name', 'email', 'body' and at least 1 comment")
  public void testGetPostComments(int postId) {
    logger.info("Executing GET ID: {} with comments", postId);

    given()
        .baseUri(testConfig.getBaseUrl())
        .pathParam("postId", postId)
        .when()
        .get(POST_COMMENTS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("size()", greaterThan(0))
        .body("[0]", hasKey("id"))
        .body("[0]", hasKey("name"))
        .body("[0]", hasKey("email"))
        .body("[0]", hasKey("body"))
        .body("[0].postId", equalTo(postId));
  }

  @Test(groups = {"get", "comments"}, description = "Verify random valid comment"
      + " has at least the following:"
      + "* 'email', 'name', 'body' fields;"
      + "* no null or empty values;"
      + "* all comments belongs to the specified post")
  public void testCommentStructureAndContentWithRandomId() {
    int postId = getRandomId();
    logger.info("Testing random valid random comment structure and content for post {}", postId);

    Response response = given()
        .baseUri(testConfig.getBaseUrl())
        .pathParam("postId", postId)
        .when()
        .get(POST_COMMENTS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().response();

    List<String> emails = response.jsonPath().getList("email");
    List<String> names = response.jsonPath().getList("name");
    List<String> bodies = response.jsonPath().getList("body");

    // Verify no empty values
    Assert.assertTrue(emails.stream()
            .noneMatch(String::isEmpty)
        , "Every comment should have email");
    Assert.assertTrue(names.stream()
            .noneMatch(String::isEmpty)
        , "Every comment should have name");
    Assert.assertTrue(bodies.stream()
            .noneMatch(String::isEmpty)
        , "Every comment should have body");

    // Verify all comments belong to the specified post
    List<Integer> postIdList = response.jsonPath()
        .getList("postId");
    Assert.assertTrue(postIdList.stream()
            .allMatch(id -> id.equals(postId))
        , "Every comment should belongs to the post");
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
