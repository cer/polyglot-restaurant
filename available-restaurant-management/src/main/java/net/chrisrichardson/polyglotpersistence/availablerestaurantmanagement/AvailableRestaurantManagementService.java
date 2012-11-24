package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import java.util.Date;
import java.util.List;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.polyglotpersistence.util.Address;

public interface AvailableRestaurantManagementService {

  void processEvent(JsonEntityCrudEvent event);

  List<AvailableRestaurant> findAvailableRestaurants(Address deliveryAddress, Date deliveryTime);

}
