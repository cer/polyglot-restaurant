package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;

public class AvailableRestaurant {

  private String name;
  private int id;

  public AvailableRestaurant() {
  }

  public AvailableRestaurant(int id, String name) {
    this.id = id;
    this.name = name;
  }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


}
