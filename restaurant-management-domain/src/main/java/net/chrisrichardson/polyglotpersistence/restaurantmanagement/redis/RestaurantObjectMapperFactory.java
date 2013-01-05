package net.chrisrichardson.polyglotpersistence.restaurantmanagement.redis;

import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.TimeRange;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RestaurantObjectMapperFactory {

  interface TimeRangeMixin {
    @JsonIgnore
    int getOpenHour();

    @JsonIgnore
    int getOpenMinute();

    @JsonIgnore
    int getCloseHour();

    @JsonIgnore
    int getCloseMinute();
  }

  interface RestaurantMixin {
    @JsonIgnore
    String getRestaurantId();
  }

  public ObjectMapper makeRestaurantObjectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.getSerializationConfig().addMixInAnnotations(TimeRange.class, TimeRangeMixin.class);
    om.getSerializationConfig().addMixInAnnotations(Restaurant.class, RestaurantMixin.class);
    om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return om;
  }

}
