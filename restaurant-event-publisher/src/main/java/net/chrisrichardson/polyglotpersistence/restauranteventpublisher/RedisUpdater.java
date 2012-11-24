package net.chrisrichardson.polyglotpersistence.restauranteventpublisher;

import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;

public interface RedisUpdater {

  void processEvent(JsonEntityCrudEvent event);

}
