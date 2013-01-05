package net.chrisrichardson.polyglotpersistence.restaurantmanagement.services;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.SimpleAvailableRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {

  @Autowired
  private SimpleAvailableRestaurantRepository restaurantRepository;

  @Override
  public Restaurant findById(int id) {
      return restaurantRepository.findDetailsById(id);
  }

}
