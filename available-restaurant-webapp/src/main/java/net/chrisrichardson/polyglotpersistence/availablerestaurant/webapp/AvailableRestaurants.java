package net.chrisrichardson.polyglotpersistence.availablerestaurant.webapp;

import java.util.List;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;

public class AvailableRestaurants {
  private List<AvailableRestaurant> availableRestaurants;

  public AvailableRestaurants() {

  }

  public AvailableRestaurants(List<AvailableRestaurant> availableRestaurants) {
    this.availableRestaurants = availableRestaurants;
  }

  public List<AvailableRestaurant> getAvailableRestaurants() {
    return availableRestaurants;
  }

  public void setAvailableRestaurants(List<AvailableRestaurant> availableRestaurants) {
    this.availableRestaurants = availableRestaurants;
  }
}
