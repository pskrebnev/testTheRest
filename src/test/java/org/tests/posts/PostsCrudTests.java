package org.tests.posts;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostsCrudTests {
  @Test
  public void testGetPosts() {
    Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts");
    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertFalse(response.getBody().asString().isEmpty(), "Response body is empty");
  }
}
