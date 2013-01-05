package net.chrisrichardson.polyglotrestaurant.test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.chrisrichardson.crudevents.repository.EntityCrudEventRepository;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.AvailableRestaurantManagementService;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.DeliveryTime;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.RestaurantTestData;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.RestaurantMother;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.service.RestaurantManagementService;
import net.chrisrichardson.polyglotpersistence.util.Address;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This is pretty ugly: trying to run three separate Spring apps together in the
 * same test. It might be cleaner to create three separate app ctxs - one per
 * app Need a way to pick the configs for each
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/appctx/*.xml", "classpath*:/appctx/restaurant-management-session-factory.xml" })
public class EndToEndRestaurantTest {

  @Autowired
  private RestaurantManagementService restaurantManagementService;

  @Autowired
  private EntityCrudEventRepository entityCrudEventRepository;

  @Autowired
  private HibernateTemplate hibernateTemplate;

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Autowired
  private AvailableRestaurantManagementService service;

  @Before
  public void initializeTheWorld() {
    // FIXME - copied from RestaurantManagementServiceIntegrationTest
    List<?> found = hibernateTemplate
        .find("select distinct r from Restaurant r inner join fetch r.menuItems inner join fetch r.serviceArea inner join fetch r.openingHours");
    hibernateTemplate.deleteAll(found);
    entityCrudEventRepository.deleteAll();
    redisTemplate.execute(new RedisCallback<String>() {

      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        connection.flushAll();
        return null;
      }
    });
  }

  @Test
  public void testEndToEnd() throws InterruptedException {
    Restaurant r = RestaurantMother.makeRestaurant();
    restaurantManagementService.add(r);

    assertEventuallySucceeds(new Runnable() {
      @Override
      public void run() {
        DeliveryTime deliveryTime = RestaurantTestData.makeGoodDeliveryTime();
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
        assertFoundAjanta(results);
      }
    });

    assertEventuallySucceeds(new Runnable() {
      @Override
      public void run() {
        DeliveryTime deliveryTime = RestaurantTestData.makeDeliveryTime(Calendar.MONDAY, 8, 0);
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
        Assert.assertEquals(Collections.EMPTY_LIST, results);
      }
    });

    Restaurant r2 = restaurantManagementService.findById(r.getId());
    r2.getOpeningHours().clear();
    r2.getOpeningHours().addAll(RestaurantMother.makeEggShopRestaurant().getOpeningHours());

    restaurantManagementService.update(r2);

    assertEventuallySucceeds(new Runnable() {
      @Override
      public void run() {
        DeliveryTime deliveryTime = RestaurantTestData.makeDeliveryTime(Calendar.MONDAY, 8, 0);
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
        assertFoundAjanta(results);
      }
    });

    assertEventuallySucceeds(new Runnable() {
      @Override
      public void run() {
        DeliveryTime deliveryTime = RestaurantTestData.makeGoodDeliveryTime();
        Address deliveryAddress = RestaurantTestData.getADDRESS1();
        List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
        Assert.assertEquals(Collections.EMPTY_LIST, results);
      }
    });
  }

  private void assertEventuallySucceeds(Runnable runnable) {
    DateTime deadline = new DateTime().plusSeconds(10);
    while (true) {
      try {
        runnable.run();
        return;
      } catch (Throwable e) {
        if (new DateTime().isAfter(deadline))
          throw new RuntimeException(e);
        try {
          TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException e1) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private void assertFoundAjanta(List<AvailableRestaurant> results) {
    assertFoundRestaurant("Ajanta", results);
  }

  private void assertFoundRestaurant(String expectedName, List<AvailableRestaurant> results) {
    Assert.assertEquals(1, results.size());
    Assert.assertEquals(expectedName, results.iterator().next().getName());
  }

}
