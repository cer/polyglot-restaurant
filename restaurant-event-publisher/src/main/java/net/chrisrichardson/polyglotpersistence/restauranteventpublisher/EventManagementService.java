package net.chrisrichardson.polyglotpersistence.restauranteventpublisher;

import java.util.List;

import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;

public interface EventManagementService {

  List<JsonEntityCrudEvent> findEventsToProcess();

  void update(List<JsonEntityCrudEvent> events);

}