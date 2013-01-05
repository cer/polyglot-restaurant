package net.chrisrichardson.polyglotrestaurant.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.chrisrichardson.polyglotpersistence.common.JacksonHelper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RestaurantEndToEndCfTest {

  private DefaultHttpClient httpClient;
  private int foundId;

    @Before
  public void setUp() {
    httpClient = new DefaultHttpClient();
  }

  @Test
  public void testEndToEnd() throws Exception {
    String restaurantName = "tastyfood" + System.currentTimeMillis();
    createRestaurant(restaurantName);
    verifyIsRestaurantIsAvailable(restaurantName);

    HttpGet get = new HttpGet(
              "http://available-restaurant.cloudfoundry.com/restaurant/" + foundId);

    try {
      HttpResponse response = httpClient.execute(get);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      String json = IOUtils.toString(response.getEntity().getContent());
      JacksonHelper jsonHelper = new JacksonHelper();
      System.out.println("json=" + json);
      Map map = jsonHelper.fromJson(json, Map.class);
      Assert.assertEquals(restaurantName, map.get("name"));
    } finally {
      get.releaseConnection();
    }
  }

  private void verifyIsRestaurantIsAvailable(String restaurantName) throws Exception {
    for (int i = 0; i < 10 && !isAvailable(restaurantName); i++)
      TimeUnit.MILLISECONDS.sleep(250);
    Assert.assertTrue(isAvailable(restaurantName));
  }

  private boolean isAvailable(String restaurantName) throws Exception {
    HttpGet get = new HttpGet(
        "http://available-restaurant.cloudfoundry.com/availablerestaurants?zipcode=94619&dayOfWeek=1&hour=18&minute=15");
    try {
      HttpResponse response = httpClient.execute(get);
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      String json = IOUtils.toString(response.getEntity().getContent());
      JacksonHelper jsonHelper = new JacksonHelper();
      Map map = jsonHelper.fromJson(json, Map.class);
      System.out.println(map);
      for (Map restaurant : (List<Map>) map.get("availableRestaurants")) {
        String name = (String) restaurant.get("name");
        if (restaurantName.equals(name)) {
            foundId = (Integer) restaurant.get("id");
            return true;
        }
      }
      return false;
    } finally {
      get.releaseConnection();
    }
  }

  private void createRestaurant(String restaurantName) throws IOException {
    String template = FileUtils.readFileToString(new File("src/test/resources/createrestauranttemplate.json"));
    String request = template.replace("RESTAURANT_NAME", restaurantName);
    HttpPost post = new HttpPost("http://restaurant-management.cloudfoundry.com/restaurants");
    post.setEntity(new StringEntity(request, ContentType.create("application/json")));

    HttpResponse response = httpClient.execute(post);
    try {
      Assert.assertEquals(200, response.getStatusLine().getStatusCode());
      String restaurantUrl = response.getFirstHeader("location").getValue();
      Assert.assertNotNull(restaurantUrl);
    } finally {
      post.releaseConnection();
    }
  }
}
