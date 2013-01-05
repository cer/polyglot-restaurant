package net.chrisrichardson.polyglotpersistence.restaurantmanagement.services;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;

public interface RestaurantService {

  Restaurant findById(int id);

}
