package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain;

public class DeliveryTime {
  private int dayOfWeek;
  private int hour;
  private int minute;

  public DeliveryTime(int dayOfWeek, int hour, int minute) {
    this.dayOfWeek = dayOfWeek;
    this.hour = hour;
    this.minute = minute;
  }

  public int getDayOfWeek() {
    return dayOfWeek;
  }

  public int getTimeOfDay() {
    return hour * 100 + minute;
  }
}
