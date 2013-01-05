package net.chrisrichardson.polyglotpersistence.restaurantmanagement.webapp.tests;

import net.chrisrichardson.polyglotpersistence.ordermanagement.service.OrderInfo;
import net.chrisrichardson.polyglotpersistence.ordermanagement.service.OrderLineItemInfo;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.MenuItem;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.RestaurantMother;
import net.chrisrichardson.polyglotpersistence.webtestutil.JettyLauncher;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class RestaurantManagementWebTest {

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
  public void testCreate() {
    Restaurant restaurant = RestaurantMother.makeRestaurant();
    URI restaurantLoc = restTemplate.postForLocation(jetty.makeUrl("restaurants"), restaurant);
    Restaurant restaurant2 = restTemplate.getForEntity(restaurantLoc, Restaurant.class).getBody();
    OrderInfo oi = makeOrderInfo(restaurant2);
    URI orderLoc = restTemplate.postForLocation(jetty.makeUrl("orders"), oi);
    System.out.println("orderLoc=" + orderLoc);
    Assert.assertNotNull(orderLoc);
    /*
    URI accountUrl = restTemplate.postForLocation(makeUrl("accounts"),
        new AccountInfo(78, "abc" + System.currentTimeMillis(), 567.8));
    System.out.println("accountUrl=" + accountUrl);
    ResponseEntity<AccountInfo> s = restTemplate.getForEntity(accountUrl,
        AccountInfo.class);
    System.out.println(s);
    System.out.println(s.getBody());
    */
  }

  private OrderInfo makeOrderInfo(Restaurant restaurant) {
    OrderInfo oi = new OrderInfo();
    oi.setRestaurantId(restaurant.getId());
    List<OrderLineItemInfo> lineItems = new ArrayList<OrderLineItemInfo>();
    for (MenuItem mi : restaurant.getMenuItems())
      lineItems.add(new OrderLineItemInfo(mi.getName(), 2));
    oi.setOrderLineItems(lineItems);
    return oi;
  }


}
