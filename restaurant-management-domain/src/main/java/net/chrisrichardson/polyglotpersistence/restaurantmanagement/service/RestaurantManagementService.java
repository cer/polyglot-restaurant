package net.chrisrichardson.polyglotpersistence.restaurantmanagement.service;

import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;

public interface RestaurantManagementService {

  void add(Restaurant restaurant);

  void update(Restaurant restaurant);

  Restaurant findById(int id);

  void delete(int id);

}
