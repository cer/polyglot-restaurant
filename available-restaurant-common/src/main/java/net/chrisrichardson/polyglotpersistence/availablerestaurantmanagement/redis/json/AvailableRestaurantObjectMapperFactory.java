package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.json;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.TimeRange;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class AvailableRestaurantObjectMapperFactory {

  interface TimeRangeMixin {
    @JsonIgnore int getOpenHour();
    @JsonIgnore int getOpenMinute();
    @JsonIgnore int getCloseHour();
    @JsonIgnore int getCloseMinute();
  }
  
  interface RestaurantMixin {
    @JsonIgnore String getRestaurantId(); 
  }

  public ObjectMapper makeRestaurantObjectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.getSerializationConfig().addMixInAnnotations(TimeRange.class, TimeRangeMixin.class);
    om.getSerializationConfig().addMixInAnnotations(Restaurant.class, RestaurantMixin.class);
    return om;
  }

}
