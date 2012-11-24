package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.chrisrichardson.crudevents.events.EntityCrudEventType;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.RestaurantMother;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.RestaurantTestData;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.testutil.DatabaseInitializer;
import net.chrisrichardson.polyglotpersistence.util.Address;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/appctx/*.xml" })
public class AvailableRestaurantManagementAmqpIntegrationTest {

  @Autowired
  @Qualifier("eventChannel")
  private MessageChannel eventChannel;

  @Autowired
  private DatabaseInitializer databaseInitializer;

  @Autowired
  private AvailableRestaurantManagementService service;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  private Message readCrudEventMessage() {
    return rabbitTemplate.receive("crudEvents");
  }

  @Test
  public void testSomething() {

    Message m;
    while ((m = readCrudEventMessage()) != null) {
      System.out.println("skipping message:  " + m);
    }

    databaseInitializer.initialize();
    publishRestaurantCrudEvent();
    assertRestaurantWasCreated();
  }

  private void publishRestaurantCrudEvent() {
    Restaurant restaurant = RestaurantMother.makeRestaurant();
    JsonEntityCrudEvent jev = JsonEntityCrudEventMother.makeJsonEntityCrudEvent(99, EntityCrudEventType.CREATE, restaurant);
    MessagingTemplate template = new MessagingTemplate(eventChannel);
    System.out.println("Sending event: " + jev);
    template.convertAndSend(jev);
  }

  private void assertRestaurantWasCreated() {
    Date deliveryTime = RestaurantTestData.makeGoodDeliveryTime();
    Address deliveryAddress = RestaurantTestData.getADDRESS1();
    System.out.println("Checking");
    try {
      TimeUnit.MILLISECONDS.sleep(500);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    List<AvailableRestaurant> results = service.findAvailableRestaurants(deliveryAddress, deliveryTime);
    assertFoundAjanta(results);
  }

  private void assertFoundAjanta(List<AvailableRestaurant> results) {
    assertFoundRestaurant("Ajanta", results);
  }

  private void assertFoundRestaurant(String expectedName, List<AvailableRestaurant> results) {
    Assert.assertEquals(1, results.size());
    Assert.assertEquals(expectedName, results.iterator().next().getName());
  }

}
