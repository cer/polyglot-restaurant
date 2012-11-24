package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.config;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.AvailableRestaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.redis.json.AvailableRestaurantObjectMapperFactory;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

  @Value("${databaseHostName}")
  protected String databaseHostName;
  
  @Autowired
  private AvailableRestaurantObjectMapperFactory restaurantObjectMapperFactory;
  
  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(factory);
    return template;
  }

  private JacksonJsonRedisSerializer<Restaurant> makeRestaurantJsonSerializer() {
    JacksonJsonRedisSerializer<Restaurant> serializer = new JacksonJsonRedisSerializer<Restaurant>(Restaurant.class);
    ObjectMapper om = restaurantObjectMapperFactory.makeRestaurantObjectMapper();
    serializer.setObjectMapper(om);
    return serializer;
  }

  private JacksonJsonRedisSerializer<AvailableRestaurant> makeAvailableRestaurantJsonSerializer() {
    JacksonJsonRedisSerializer<AvailableRestaurant> serializer = new JacksonJsonRedisSerializer<AvailableRestaurant>(AvailableRestaurant.class);
    ObjectMapper om = new ObjectMapper();
    serializer.setObjectMapper(om);
    om.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return serializer;
  }

  @Bean
  @Qualifier("Restaurant")
  public RedisTemplate<String, Restaurant> restaurantTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Restaurant> template = new RedisTemplate<String, Restaurant>();
    template.setConnectionFactory(factory);
    template.setDefaultSerializer(new StringRedisSerializer());
    JacksonJsonRedisSerializer<Restaurant> jsonSerializer = makeRestaurantJsonSerializer();
    template.setValueSerializer(jsonSerializer);
    return template;
  }

  @Bean
  @Qualifier("AvailableRestaurant")
  public RedisTemplate<String, AvailableRestaurant> availableRestaurantTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, AvailableRestaurant> template = new RedisTemplate<String, AvailableRestaurant>();
    template.setConnectionFactory(factory);
    template.setDefaultSerializer(new StringRedisSerializer());
    JacksonJsonRedisSerializer<AvailableRestaurant> jsonSerializer = makeAvailableRestaurantJsonSerializer();
    template.setValueSerializer(jsonSerializer);
    return template;
  }


}
