package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import java.util.Date;
import java.util.List;

import net.chrisrichardson.crudevents.events.EntityCrudEventType;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.SimpleAvailableRestaurantRepository;
import net.chrisrichardson.polyglotpersistence.common.JacksonHelper;
import net.chrisrichardson.polyglotpersistence.util.Address;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AvailableRestaurantManagementServiceImpl implements AvailableRestaurantManagementService {

  private Log logger = LogFactory.getLog(getClass());
  
  @Autowired
  private JacksonHelper jacksonHelper;

  @Autowired
  private SimpleAvailableRestaurantRepository availableRestaurantRepository;

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @Override
  public void processEvent(JsonEntityCrudEvent event) {
    try {
      Restaurant r = jacksonHelper.fromJson(event.getJson(), Restaurant.class);
      EntityCrudEventType eventType = EntityCrudEventType.valueOf(event.getEventType());
      applyEvent(event.getId(), eventType, r);
    } catch (Exception e) {
      logger.error("exception processing event", e);
    }
  }

  private void applyEvent(int eventId, EntityCrudEventType type, Restaurant restaurant) {
    switch (type) {
    case CREATE:
      applyCreateEvent(eventId, restaurant);
      break;
    case UPDATE:
      applyUpdateEvent(eventId, restaurant);
      break;
    default:
      throw new UnsupportedOperationException("implement me: " + type);
    }
  }

  // FIXME There is probably an issue with CREATE arriving before UPDATE

  private void applyUpdateEvent(final int eventId, final Restaurant restaurant) {
    new UpdateTransaction(availableRestaurantRepository, stringRedisTemplate, eventId, restaurant).execute();
  }

  private void applyCreateEvent(final int eventId, final Restaurant restaurant) {
    new CreateTransaction(availableRestaurantRepository, stringRedisTemplate, eventId, restaurant).execute();
  }

  @Override
  public List<AvailableRestaurant> findAvailableRestaurants(Address deliveryAddress, Date deliveryTime) {
    return availableRestaurantRepository.findAvailableRestaurants(deliveryAddress, deliveryTime);
  }

}
