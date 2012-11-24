package net.chrisrichardson.polyglotpersistence.restauranteventpublisher;

import java.util.List;

import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EntityCrudEventProcessor {

  private Log logger = LogFactory.getLog(getClass());
  private final EventManagementService eventManagementService;
  private final RedisUpdater redisUpdater;

  @Autowired
  public EntityCrudEventProcessor(EventManagementService eventManagementService, RedisUpdater redisUpdater) {
    this.eventManagementService = eventManagementService;
    this.redisUpdater = redisUpdater;
  }

  @Scheduled(fixedDelay = 3000)
  public void publishEvents() {
    List<JsonEntityCrudEvent> events = eventManagementService.findEventsToProcess();
    logger.debug("Got events: " + events.size());
    if (events.isEmpty())
      return;
    processEvents(events);
    markAsProcessed(events);
    eventManagementService.update(events);
  }

  private void markAsProcessed(List<JsonEntityCrudEvent> events) {
    for (JsonEntityCrudEvent event : events) {
      event.setProcessed(true);
    }
  }

  private void processEvents(List<JsonEntityCrudEvent> events) {
    for (JsonEntityCrudEvent event : events) {
      redisUpdater.processEvent(event);

    }
  }

}
