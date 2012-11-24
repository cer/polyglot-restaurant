package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.SimpleAvailableRestaurantRepository;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.util.RedisUtil;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

class CreateTransaction extends RedisTransaction {

  private final StringRedisTemplate stringRedisTemplate;
  private final SimpleAvailableRestaurantRepository availableRestaurantRepository;
  private final String versionKey;
  private final Restaurant restaurant;
  private final int eventId;

  public CreateTransaction(SimpleAvailableRestaurantRepository availableRestaurantRepository,
      StringRedisTemplate stringRedisTemplate, int eventId, Restaurant restaurant) {
    super(stringRedisTemplate);
    this.stringRedisTemplate = stringRedisTemplate;
    this.availableRestaurantRepository = availableRestaurantRepository;
    this.eventId = eventId;
    this.restaurant = restaurant;
    versionKey = RedisUtil.key("restaurant", "eventId", restaurant.getId());
  }

  @Override
  protected void doReads() {
    stringRedisTemplate.watch(versionKey);
    String currentVersion = stringRedisTemplate.opsForValue().get(versionKey);
    Assert.isNull(currentVersion);
  }

  @Override
  protected void doWrites() {
    stringRedisTemplate.opsForValue().set(versionKey, Integer.toString(eventId));
    availableRestaurantRepository.add(restaurant);
  }

}