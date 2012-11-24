package net.chrisrichardson.polyglotpersistence.restauranteventpublisher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.chrisrichardson.crudevents.events.EntityCrudEvent;
import net.chrisrichardson.crudevents.events.EntityCrudEventType;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.crudevents.repository.EntityCrudEventRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/appctx/*.xml" })
public class RestaurantEventPublisherIntegrationTest {

  @Autowired
  private EntityCrudEventProcessor publisher;

  @Autowired
  private EntityCrudEventRepository eventRepository;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Test
  public void insertingEventShouldSendAmqpMessage() {
    initializeDatabase();
    insertEvent();
    runEventPublisher();
    assertThatEventIsMarkedAsPublished();
    assertAMessageProduced();
  }

  private void initializeDatabase() {
    eventRepository.deleteAll();
    Message m;
    while ((m = readCrudEventMessage()) != null) {
      System.out.println("skipping message:  " + m);
    }
  }

  private void insertEvent() {
    Restaurant restaurant = new Restaurant();
    EntityCrudEvent event = new EntityCrudEvent(EntityCrudEventType.CREATE, restaurant.getId(), restaurant);
    eventRepository.add(event);
  }

  private void runEventPublisher() {
    publisher.publishEvents();
  }

  private void assertThatEventIsMarkedAsPublished() {
    List<JsonEntityCrudEvent> events = eventRepository.findAll();
    Assert.assertEquals(1, events.size());
    Assert.assertTrue(events.get(0).isProcessed());

  }

  private void assertAMessageProduced() {
    for (int i = 0; i < 10; i++) {
      Message message = readCrudEventMessage();
      if (message != null) {
        return;
      }
      try {
        System.out.println("Sleeping");
        TimeUnit.MICROSECONDS.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    Assert.fail("didn't receive message");
  }

  private Message readCrudEventMessage() {
    return rabbitTemplate.receive("crudEvents");
  }

}
