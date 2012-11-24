package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.SimpleAvailableRestaurantRepository;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;
import net.chrisrichardson.polyglotpersistence.util.DateTimeUtil;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.util.AvailableRestaurantKeys;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.util.RedisEntityKeyFormatter;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.TimeRange;
import net.chrisrichardson.polyglotpersistence.util.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SimpleAvailableRestaurantRepositoryImpl implements SimpleAvailableRestaurantRepository {

  private final StringRedisTemplate redisTemplate;

  private RedisEntityKeyFormatter keyFormatter = new RedisEntityKeyFormatter(AvailableRestaurant.class);

  private RedisTemplate<String, Restaurant> restaurantTemplate;
  private final RedisTemplate<String, AvailableRestaurant> availableRestaurantTemplate;

  
  @Autowired
  public SimpleAvailableRestaurantRepositoryImpl(StringRedisTemplate redisTemplate, 
                                                RedisConnectionFactory redisConnectionFactory,
                                                @Qualifier("Restaurant") RedisTemplate<String, Restaurant> restaurantTemplate,
                                                @Qualifier("AvailableRestaurant") RedisTemplate<String, AvailableRestaurant> availableRestaurantTemplate
                                                ) {
    this.redisTemplate = redisTemplate;
    this.restaurantTemplate = restaurantTemplate;
    this.availableRestaurantTemplate = availableRestaurantTemplate;
  }

  @Override
  public void add(final Restaurant restaurant) {
    addRestaurantDetails(restaurant);
    addAvailabilityIndexEntries(restaurant);
  }

  private void addRestaurantDetails(Restaurant restaurant) {
    restaurantTemplate.opsForValue().set(keyFormatter.key(restaurant.getId()), restaurant);
  }
  
  private void addAvailabilityIndexEntries(Restaurant restaurant) {
    for (TimeRange tr : restaurant.getOpeningHours()) {
      String indexValue = formatTrId(restaurant, tr);
      int dayOfWeek = tr.getDayOfWeek();
      int closingTime = tr.getClosingTime();
      for (String zipCode : restaurant.getServiceArea()) {
        redisTemplate.opsForZSet().add(closingTimesKey(zipCode, dayOfWeek), indexValue, closingTime);
      }
    }
  }

  private String closingTimesKey(String zipCode, int dayOfWeek) {
    return AvailableRestaurantKeys.closingTimesKey(zipCode, dayOfWeek);
  }
  
  private String formatTrId(Restaurant restaurant, TimeRange tr) {
    return tr.getOpeningTime() + "_" + restaurant.getId();
  }

  @Override
  public List<AvailableRestaurant> findAvailableRestaurants(Address deliveryAddress, Date deliveryTime) {
    String zipCode = deliveryAddress.getZip();
    int dayOfWeek = DateTimeUtil.dayOfWeek(deliveryTime);
    int timeOfDay = DateTimeUtil.timeOfDay(deliveryTime);
    String closingTimesKey = closingTimesKey(zipCode, dayOfWeek);

    Set<String> trsClosingAfter = redisTemplate.opsForZSet().rangeByScore(closingTimesKey, timeOfDay, 2359);
    Set<String> restaurantIds = new HashSet<String>();
    for (String tr : trsClosingAfter) {
      String[] values = tr.split("_");
      if (Integer.parseInt(values[0]) <= timeOfDay)
        restaurantIds.add(values[1]);
    }
    Collection<String> keys = keyFormatter.keys(restaurantIds);
    return availableRestaurantTemplate.opsForValue().multiGet(keys);
  }
  
  @Override
  public void delete(Restaurant restaurant) {
    for (TimeRange tr : restaurant.getOpeningHours()) {
      String trId = formatTrId(restaurant, tr);
      int dayOfWeek = tr.getDayOfWeek();
    
      for (String zipCode : restaurant.getServiceArea()) {
        redisTemplate.opsForZSet().remove(closingTimesKey(zipCode, dayOfWeek), trId);
      }
    }
    redisTemplate.delete(keyFormatter.key(restaurant.getId()));
  }
  
  @Override
  public Restaurant findDetailsById(int id) {
    return restaurantTemplate.opsForValue().get(keyFormatter.key(id));
  }  
}
