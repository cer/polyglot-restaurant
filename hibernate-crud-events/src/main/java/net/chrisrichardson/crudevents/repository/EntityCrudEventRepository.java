package net.chrisrichardson.crudevents.repository;

import java.util.List;

import net.chrisrichardson.crudevents.events.EntityCrudEvent;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;

public interface EntityCrudEventRepository {

  void add(EntityCrudEvent event);

  JsonEntityCrudEvent findNextEvent();

  void deleteAll();

  List<JsonEntityCrudEvent> findAll();

  List<JsonEntityCrudEvent> findEventsToPublish();

  void update(List<JsonEntityCrudEvent> events);

}
