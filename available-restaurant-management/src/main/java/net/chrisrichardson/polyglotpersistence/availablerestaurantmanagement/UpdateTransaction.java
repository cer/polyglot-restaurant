package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.SimpleAvailableRestaurantRepository;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.util.RedisUtil;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

class UpdateTransaction extends RedisTransaction {

  /**
	 * 
	 */
  private Restaurant existing;
  private final String versionKey;
  private final Restaurant restaurant;
  private final int eventId;
  private final StringRedisTemplate stringRedisTemplate;
  private final SimpleAvailableRestaurantRepository availableRestaurantRepository;

  public UpdateTransaction(SimpleAvailableRestaurantRepository availableRestaurantRepository,
      StringRedisTemplate stringRedisTemplate, int eventId, Restaurant restaurant) {
    super(stringRedisTemplate);
    this.availableRestaurantRepository = availableRestaurantRepository;
    this.stringRedisTemplate = stringRedisTemplate;
    this.eventId = eventId;
    this.restaurant = restaurant;
    versionKey = RedisUtil.key("restaurant", "eventId", restaurant.getId());
  }

  @Override
  protected void doReads() {
    stringRedisTemplate.watch(versionKey);
    String currentVersion = stringRedisTemplate.opsForValue().get(versionKey);
    Assert.notNull(currentVersion);
    Assert.isTrue(Integer.parseInt(currentVersion) < eventId, "CurrentVersion=" + currentVersion + ", eventId=" + eventId);
    existing = availableRestaurantRepository.findDetailsById(restaurant.getId());
    Assert.notNull(existing);
  }

  @Override
  protected void doWrites() {
    stringRedisTemplate.opsForValue().set(versionKey, Integer.toString(eventId));
    availableRestaurantRepository.delete(existing);
    availableRestaurantRepository.add(restaurant);
  }

}