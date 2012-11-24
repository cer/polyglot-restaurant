package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.util;


public class AvailableRestaurantKeys {

  public static String closingTimesKey(String zipCode, int dayOfWeek) {
    return RedisUtil.key("closingTimes", zipCode, dayOfWeek);
  }

}
