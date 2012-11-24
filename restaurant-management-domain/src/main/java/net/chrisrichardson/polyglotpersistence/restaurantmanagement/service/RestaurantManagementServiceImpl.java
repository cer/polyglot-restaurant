package net.chrisrichardson.polyglotpersistence.restaurantmanagement.service;

import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RestaurantManagementServiceImpl implements RestaurantManagementService {

  private final RestaurantRepository restaurantRepository;

  @Autowired
  public RestaurantManagementServiceImpl(RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
  }

  @Override
  public void add(Restaurant restaurant) {
    restaurantRepository.add(restaurant);
  }

  @Override
  @CacheEvict(value = "Restaurant", key = "#restaurant.id")
  public void update(Restaurant restaurant) {
    restaurantRepository.update(restaurant);
  }

  @Override
  @Cacheable(value = "Restaurant")
  public Restaurant findById(int id) {
    return restaurantRepository.findRestaurant(id);
  }

  @Override
  @CacheEvict(value = "Restaurant")
  public void delete(int id) {
    // FIXME JacksonJSON gets NPE with serviceArea if we don't do this
    findById(id);
    restaurantRepository.delete(id);
  }

}
