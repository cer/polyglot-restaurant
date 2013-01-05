package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.chrisrichardson.crudevents.events.EntityCrudEventType;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.*;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.testutil.DatabaseInitializer;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.testutil.IdentityAssigner;
import net.chrisrichardson.polyglotpersistence.util.Address;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/appctx/*.xml" })
public class AvailableRestaurantManagementServiceIntegrationTest {

  @Autowired
  private AvailableRestaurantManagementService service;

  @Autowired
  private DatabaseInitializer databaseInitializer;

  private Restaurant ajantaRestaurant;

  private Restaurant eggshopRestaurant;

  private Restaurant lateNightSnack;

  @Before
  public void initialize() {
    databaseInitializer.initialize();
    ajantaRestaurant = RestaurantMother.makeRestaurant();
    eggshopRestaurant = RestaurantMother.makeEggShopRestaurant();
    lateNightSnack = RestaurantMother.makeLateNightTacos();

    identityAssigner.assignIdentities(ajantaRestaurant, eggshopRestaurant, lateNightSnack);
  }

  private IdentityAssigner identityAssigner = new IdentityAssigner();

  private void assertFoundAjanta(List<AvailableRestaurant> results) {
    assertFoundRestaurant("Ajanta", results);
  }

  private void assertFoundRestaurant(String expectedName, List<AvailableRestaurant> results) {
    Assert.assertEquals(1, results.size());
    Assert.assertEquals(expectedName, results.iterator().next().getName());
  }

  private int eventId;

  private void add(Restaurant restaurant) {
    JsonEntityCrudEvent event = JsonEntityCrudEventMother
        .makeJsonEntityCrudEvent(eventId++, EntityCrudEventType.CREATE, restaurant);
    service.processEvent(event);
  }

  private void update(Restaurant restaurant) {
    JsonEntityCrudEvent event = JsonEntityCrudEventMother
        .makeJsonEntityCrudEvent(eventId++, EntityCrudEventType.UPDATE, restaurant);
    service.processEvent(event);
  }

  @Test
  public void testSomething() {
    add(ajantaRestaurant);
    add(eggshopRestaurant);
    add(lateNightSnack);

    DeliveryTime deliveryTime = RestaurantTestData.makeGoodDeliveryTime();
    Address deliveryAddress = RestaurantTestData.getADDRESS1();
    List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
    assertFoundAjanta(results);
  }

  @Test
  public void testFindAvailableRestaurants_Monday8Am() {
    add(ajantaRestaurant);
    add(eggshopRestaurant);
    add(lateNightSnack);

    DeliveryTime deliveryTime = RestaurantTestData.makeDeliveryTime(Calendar.MONDAY, 8, 0);
    Address deliveryAddress = RestaurantTestData.getADDRESS1();
    List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
    assertFoundRestaurant(RestaurantMother.MONTCLAIR_EGGSHOP, results);
  }

  @Test
  public void testFindAvailableRestaurants_None() {

    add(ajantaRestaurant);
    add(eggshopRestaurant);
    add(lateNightSnack);

    DeliveryTime deliveryTime = RestaurantTestData.makeBadDeliveryTime();
    Address deliveryAddress = RestaurantTestData.getADDRESS1();
    List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
    Assert.assertTrue(results.isEmpty());
  }

  private void updateRestaurantOpeningHours() {
    ajantaRestaurant.setOpeningHours(RestaurantMother.makeOpeningHours(RestaurantMother.OPENING_MINUTE - 1));
  }

  @Test
  public void testUpdateRestaurant() {
    add(ajantaRestaurant);
    add(eggshopRestaurant);
    add(lateNightSnack);

    updateRestaurantOpeningHours();
    update(ajantaRestaurant);

    DeliveryTime deliveryTime = RestaurantTestData.getTimeTomorrow(RestaurantMother.OPENING_HOUR, RestaurantMother.OPENING_MINUTE - 1);
    Address deliveryAddress = RestaurantTestData.getADDRESS1();
    List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
    assertFoundAjanta(results);
  }
}
