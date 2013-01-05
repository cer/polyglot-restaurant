package net.chrisrichardson.polyglotpersistence.availablerestaurant.webtests;

import net.chrisrichardson.polyglotpersistence.availablerestaurant.webapp.AvailableRestaurants;
import net.chrisrichardson.polyglotpersistence.webtestutil.JettyLauncher;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AvailableRestaurantWebTest {

  private static JettyLauncher jetty;
  private static RestTemplate restTemplate;

  @BeforeClass
  public static void initialize() throws Exception {
    jetty = new JettyLauncher();
    jetty.setContextPath("/webapp");
    jetty.setPort(-1);
    jetty.setSrcWebApp("src/main/webapp");
    jetty.start();
    restTemplate = new RestTemplate();
  }

  @AfterClass
  public static void tearDown() throws InterruptedException {
    jetty.stop();
  }

  @Test
  public void test() {
    ResponseEntity<AvailableRestaurants> available = restTemplate.getForEntity(jetty.makeUrl("availablerestaurants?zipcode=94619&dayOfWeek=1&hour=18&minute=15"), AvailableRestaurants.class);
    Assert.assertEquals(HttpStatus.OK, available.getStatusCode());
    System.out.println("available=" + available.getBody().getAvailableRestaurants());

  }


}
