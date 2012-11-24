package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

public abstract class RedisTransaction {

  private final StringRedisTemplate stringRedisTemplate;

  protected RedisTransaction(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  public Boolean execute() {
    return stringRedisTemplate.execute(new SessionCallback<Boolean>() {
      @Override
      public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
        doReads();
        stringRedisTemplate.multi();
        doWrites();
        List<Object> result = stringRedisTemplate.exec();
        // result is null if the txn failed.
        return result != null;
      }
    });

  }

  protected abstract void doReads();

  protected abstract void doWrites();

}