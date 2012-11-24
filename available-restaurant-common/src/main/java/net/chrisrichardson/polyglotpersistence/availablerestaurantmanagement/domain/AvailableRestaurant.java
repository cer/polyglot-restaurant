package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;

public class AvailableRestaurant {

  private String name;

  public AvailableRestaurant() {
  }
  
  public AvailableRestaurant(String name) {
    this.name = name;
  }

  public AvailableRestaurant(Restaurant restaurant) {
    this.name = restaurant.getName();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


}
