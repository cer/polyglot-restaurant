package net.chrisrichardson.polyglotpersistence.restauranteventpublisher;

import java.util.List;

import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.crudevents.repository.EntityCrudEventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventDeliveryServiceImpl implements EventManagementService {

  @Autowired
  private EntityCrudEventRepository entityCrudEventRepository;

  @Override
  public List<JsonEntityCrudEvent> findEventsToProcess() {
    return entityCrudEventRepository.findEventsToPublish();
  }

  @Override
  public void update(List<JsonEntityCrudEvent> events) {
    entityCrudEventRepository.update(events);

  }
}
