/*
 * Copyright (c) 2005 Chris Richardson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain;

import net.chrisrichardson.polyglotpersistence.util.Address;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Restaurant {

  private int id;

  private int version;

  private String name;

  private Set<String> serviceArea;

  private String notificationEmailAddress;

  private List<MenuItem> menuItems;

  private Set<TimeRange> openingHours;

  private String type;

  public Restaurant() {

  }

  /*
   * Creates a Restaurant
   */

  public Restaurant(String name, String type, Set<String> serviceArea, Set<TimeRange> openingHours, List<MenuItem> menuItems) {
    this.name = name;
    this.type = type;
    this.serviceArea = serviceArea;
    this.openingHours = openingHours;
    this.menuItems = menuItems;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getNotificationEmailAddress() {
    return notificationEmailAddress;
  }

  public Set<String> getServiceArea() {
    return serviceArea;
  }

  public List<MenuItem> getMenuItems() {
    return menuItems;
  }

  public Set<TimeRange> getOpeningHours() {
    return openingHours;
  }

  public String getRestaurantId() {
    return Integer.toString(id);
  }

  public boolean isInServiceArea(Address address, Date deliveryTime) {
    return isInServiceArea(address) && isOpenAtThisTime(deliveryTime);
  }

  private boolean isOpenAtThisTime(Date deliveryTime) {
    for (TimeRange range : openingHours) {
      if (range.isOpenAtThisTime(deliveryTime))
        return true;
    }

    return false;
  }

  public boolean isInServiceArea(Address address) {
    boolean result = getServiceArea().contains(address.getZip());
    return result;
  }

  public int getId() {
    return id;
  }

  public void setOpeningHours(Set<TimeRange> openingHours) {
    this.openingHours = openingHours;
  }

  // Extra setters

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setServiceArea(Set<String> serviceArea) {
    this.serviceArea = serviceArea;
  }

  public void setNotificationEmailAddress(String notificationEmailAddress) {
    this.notificationEmailAddress = notificationEmailAddress;
  }

  public void setMenuItems(List<MenuItem> menuItems) {
    this.menuItems = menuItems;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public int getVersion() {
    return version;
  }
}
