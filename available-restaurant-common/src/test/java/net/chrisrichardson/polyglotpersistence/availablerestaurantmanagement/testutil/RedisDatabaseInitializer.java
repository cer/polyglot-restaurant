package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.testutil;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.testutil.DatabaseInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisDatabaseInitializer implements DatabaseInitializer {
  
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @Override
  public void initialize() {
    stringRedisTemplate.execute(new RedisCallback<Void>() {

    @Override
    public Void doInRedis(RedisConnection connection)
        throws DataAccessException {
      connection.flushDb();
      return null;
    }
  });
  }

}
