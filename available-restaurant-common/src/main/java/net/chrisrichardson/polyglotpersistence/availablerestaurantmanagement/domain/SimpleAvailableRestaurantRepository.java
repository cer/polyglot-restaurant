package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain;

import java.util.Date;
import java.util.List;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.util.Address;

public interface SimpleAvailableRestaurantRepository {

  void add(Restaurant restaurant);

  List<AvailableRestaurant> findAvailableRestaurants(Address deliveryAddress, Date deliveryTime);

  // To use transactions we need to split reading from writing
  
  Restaurant findDetailsById(int id);

  void delete(Restaurant restaurant);

}
