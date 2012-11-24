package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.util;

import java.util.ArrayList;
import java.util.Collection;


public class RedisEntityKeyFormatter {

  private String keyPrefix;

  public RedisEntityKeyFormatter(Class<?> type) {
    this.keyPrefix = type.getSimpleName() + ":entity:";
  }

  public String key(int id) {
    return keyPrefix + id;
  }

  public Collection<String> keys(Collection<String> ids) {
    Collection<String> result = new ArrayList<String>();
    for (String id : ids) {
      result.add(keyPrefix + id);
    }
    return result;
  }

}
