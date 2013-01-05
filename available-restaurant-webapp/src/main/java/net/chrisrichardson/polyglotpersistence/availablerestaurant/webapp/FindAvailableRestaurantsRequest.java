package net.chrisrichardson.polyglotpersistence.availablerestaurant.webapp;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.DeliveryTime;
import net.chrisrichardson.polyglotpersistence.util.Address;
import org.apache.commons.lang.builder.ToStringBuilder;

public class FindAvailableRestaurantsRequest {

  private String zipcode;
  private int dayOfWeek;
  private int hour;
  private int minute;

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public void setDayOfWeek(int dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public void setMinute(int minute) {
    this.minute = minute;
  }

  public Address makeAddress() {
    return new Address(null, null, null, null, zipcode);
  }

  public DeliveryTime makeDeliveryTime() {
    return new DeliveryTime(dayOfWeek, hour, minute);
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
