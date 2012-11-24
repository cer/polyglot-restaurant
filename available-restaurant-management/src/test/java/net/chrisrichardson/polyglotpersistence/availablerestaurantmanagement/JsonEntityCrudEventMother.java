package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import net.chrisrichardson.crudevents.events.EntityCrudEventType;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.common.JacksonHelper;

public class JsonEntityCrudEventMother {
  static JsonEntityCrudEvent makeJsonEntityCrudEvent(int eventId, EntityCrudEventType eventType, Restaurant restaurant) {
    JacksonHelper jacksonHelper = new JacksonHelper();
    return new JsonEntityCrudEvent(eventId, eventType.toString(), Integer.toString(restaurant.getId()), restaurant.getName(),
        jacksonHelper.toJson(restaurant));
  }
}
