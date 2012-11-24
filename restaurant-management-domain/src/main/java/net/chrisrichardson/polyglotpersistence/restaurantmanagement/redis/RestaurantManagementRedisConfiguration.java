package net.chrisrichardson.polyglotpersistence.restaurantmanagement.redis;

import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RestaurantManagementRedisConfiguration {

  @Autowired
  private RestaurantObjectMapperFactory restaurantObjectMapperFactory;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    JedisConnectionFactory factory = new JedisConnectionFactory();
    factory.setHostName("localhost");
    factory.setPort(6379);
    factory.setUsePool(true);
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxActive(1000);
    factory.setPoolConfig(poolConfig);
    return factory;
  }

  @Bean
  @Qualifier("Restaurant")
  public JacksonJsonRedisSerializer<Restaurant> makeRestaurantJsonSerializer() {
    JacksonJsonRedisSerializer<Restaurant> serializer = new JacksonJsonRedisSerializer<Restaurant>(Restaurant.class);
    ObjectMapper om = restaurantObjectMapperFactory.makeRestaurantObjectMapper();
    serializer.setObjectMapper(om);
    return serializer;
  }

  @Bean
  @Qualifier("RestaurantCachingTemplate")
  public RedisTemplate<String, Restaurant> restaurantCachingTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Restaurant> template = new RedisTemplate<String, Restaurant>();
    template.setConnectionFactory(factory);
    // template.setDefaultSerializer(new StringRedisSerializer());
    JacksonJsonRedisSerializer<Restaurant> jsonSerializer = makeRestaurantJsonSerializer();
    template.setValueSerializer(jsonSerializer);
    return template;
  }

}
