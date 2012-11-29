package net.chrisrichardson.polyglotpersistence.restaurantmanagement;

import java.util.List;

import net.chrisrichardson.crudevents.events.EntityCrudEventType;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.crudevents.repository.EntityCrudEventRepository;
import net.chrisrichardson.polyglotpersistence.common.JacksonHelper;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.MenuItem;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.RestaurantMother;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.service.RestaurantManagementService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/appctx/*.xml")
public class RestaurantManagementServiceIntegrationTest {

  @Autowired
  private RestaurantManagementService restaurantManagementService;

  @Autowired
  private EntityCrudEventRepository entityCrudEventRepository;

  @Autowired
  private HibernateTemplate hibernateTemplate;

  @Autowired
  private RedisTemplate<?, ?> redisTemplate;
  
  @Autowired
  private JacksonHelper jacksonHelper;

  @Before
  public void initializeDatabase() {
    List<?> found = hibernateTemplate
        .find("select distinct r from Restaurant r inner join fetch r.menuItems inner join fetch r.serviceArea inner join fetch r.openingHours");
    hibernateTemplate.deleteAll(found);
    entityCrudEventRepository.deleteAll();
    redisTemplate.execute(new RedisCallback<String>() {

      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        connection.flushAll();
        return null;
      }
    });
  }

  
  @Test
  public void addAndUpdateRestaurant() {
    Restaurant r = RestaurantMother.makeEggShopRestaurant();
    restaurantManagementService.add(r);
    Assert.assertTrue(r.getId() > 0);
    Restaurant r2 = restaurantManagementService.findById(r.getId());
    Assert.assertNotNull(r2);
    Assert.assertEquals(r.getName(), r2.getName());
    assertEventsIncludeRestaurantCreation(r);

    String newName = r2.getName() + r2.getName();
    r2.setName(newName);
    restaurantManagementService.update(r2);

    Restaurant r3 = restaurantManagementService.findById(r.getId());
    Assert.assertEquals(r2.getName(), r3.getName());

    assertEventsIncludeUpdatedRestaurantName(r2);
  }


  private void assertEventsIncludeRestaurantCreation(Restaurant r) {
    List<JsonEntityCrudEvent> events = entityCrudEventRepository.findAll();
    Assert.assertEquals(1, events.size());
    JsonEntityCrudEvent event = events.get(0);
    Assert.assertEquals(Integer.toString(r.getId()), event.getEntityId());
    Assert.assertEquals(Restaurant.class.getName(), event.getEntityType());
    Assert.assertEquals(EntityCrudEventType.CREATE.toString(), event.getEventType());
  }


  private void assertEventsIncludeUpdatedRestaurantName(Restaurant r) {
    List<JsonEntityCrudEvent> events = entityCrudEventRepository.findAll();
    Assert.assertEquals(2, events.size());
    JsonEntityCrudEvent event = events.get(1);
    Assert.assertEquals(Integer.toString(r.getId()), event.getEntityId());
    Assert.assertEquals(EntityCrudEventType.UPDATE.toString(), event.getEventType());
    
    Restaurant r4 = jacksonHelper.fromJson(event.getJson(), Restaurant.class);
    Assert.assertEquals(r.getName(), r4.getName());
  }

  @Test
  public void addMenuItem() {
    Restaurant r = RestaurantMother.makeEggShopRestaurant();
    restaurantManagementService.add(r);
    Restaurant r2 = restaurantManagementService.findById(r.getId());
    System.out.println("RestaurantVersion=" + r2.getVersion());

    MenuItem newMenuItem = new MenuItem("Eggs and Bacon", 12.34);
    r2.getMenuItems().add(newMenuItem);
    restaurantManagementService.update(r2);

    Restaurant r3 = restaurantManagementService.findById(r.getId());
    Assert.assertEquals(1, r3.getMenuItems().size());
    Assert.assertEquals(r2.getName(), r3.getName());
    
    assertEventsIncludeUpdatedMenuItem(r2, newMenuItem);

  }


  private void assertEventsIncludeUpdatedMenuItem(Restaurant r, MenuItem newMenuItem) {
    List<JsonEntityCrudEvent> events = entityCrudEventRepository.findAll();
    Assert.assertEquals(2, events.size());
    JsonEntityCrudEvent ev = events.get(1);
    Assert.assertEquals(Integer.toString(r.getId()), ev.getEntityId());
    Assert.assertEquals(EntityCrudEventType.UPDATE.toString(), ev.getEventType());
    
    Restaurant r4 = jacksonHelper.fromJson(ev.getJson(), Restaurant.class);
    List<MenuItem> menuItems = r4.getMenuItems();
    Assert.assertEquals(r.getMenuItems().size(), menuItems.size());
    Assert.assertEquals(newMenuItem.getName(), menuItems.get(0).getName());
    Assert.assertEquals(newMenuItem.getPrice(), menuItems.get(0).getPrice(), 0.01);
  }

  @Test
  public void deleteRestaurant() {
    Restaurant r = RestaurantMother.makeEggShopRestaurant();
    restaurantManagementService.add(r);
    restaurantManagementService.delete(r.getId());
    
    assertEventsIncludeDeleteRestaurant(r);
  }


  private void assertEventsIncludeDeleteRestaurant(Restaurant r) {
    List<JsonEntityCrudEvent> events = entityCrudEventRepository.findAll();
    Assert.assertEquals(2, events.size());
    JsonEntityCrudEvent event = events.get(1);
    Assert.assertEquals(Integer.toString(r.getId()), event.getEntityId());
    Assert.assertEquals(EntityCrudEventType.DELETE.toString(), event.getEventType());
  }

  @Autowired
  private TransactionTemplate transactionTemplate;
  
  @Test
  public void insertShouldBeRolledBack() {
    transactionTemplate.execute(new TransactionCallback<Void>() {

      @Override
      public Void doInTransaction(TransactionStatus status) {
        Restaurant r = RestaurantMother.makeEggShopRestaurant();
        restaurantManagementService.add(r);
        hibernateTemplate.flush();
        Assert.assertEquals(1, entityCrudEventRepository.findAll().size());
        status.setRollbackOnly();
        return null;
      }
    });
    
    Assert.assertEquals(0, entityCrudEventRepository.findAll().size());


  }

  @Test
  public void updateShouldBeRolledBack() {
    final Restaurant r = RestaurantMother.makeEggShopRestaurant();
    restaurantManagementService.add(r);

    transactionTemplate.execute(new TransactionCallback<Void>() {
      
      @Override
      public Void doInTransaction(TransactionStatus status) {
        Restaurant r2 = restaurantManagementService.findById(r.getId());
        r2.setName(r2.getName() + r2.getName());
        restaurantManagementService.update(r2);
        hibernateTemplate.flush();
        Assert.assertEquals(2, entityCrudEventRepository.findAll().size());
        status.setRollbackOnly();
        return null;
      }
    });
    
    Assert.assertEquals(1, entityCrudEventRepository.findAll().size());
    
    
  }
  @Test
  public void deleteShouldBeRolledBack() {
    final Restaurant r = RestaurantMother.makeEggShopRestaurant();
    restaurantManagementService.add(r);
    
    transactionTemplate.execute(new TransactionCallback<Void>() {
      
      @Override
      public Void doInTransaction(TransactionStatus status) {
        restaurantManagementService.delete(r.getId());
        hibernateTemplate.flush();
        Assert.assertEquals(2, entityCrudEventRepository.findAll().size());
        status.setRollbackOnly();
        return null;
      }
    });
    
    Assert.assertEquals(1, entityCrudEventRepository.findAll().size());
  }
}
