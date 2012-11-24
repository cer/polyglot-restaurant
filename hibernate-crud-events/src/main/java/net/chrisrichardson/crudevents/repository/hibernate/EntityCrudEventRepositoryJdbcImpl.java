package net.chrisrichardson.crudevents.repository.hibernate;

import java.util.List;

import net.chrisrichardson.crudevents.events.EntityCrudEvent;
import net.chrisrichardson.crudevents.events.JsonEntityCrudEvent;
import net.chrisrichardson.crudevents.repository.EntityCrudEventRepository;
import net.chrisrichardson.polyglotpersistence.common.JacksonHelper;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EntityCrudEventRepositoryJdbcImpl implements EntityCrudEventRepository {

  @Autowired
  private JacksonHelper jacksonHelper;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ApplicationContext ctx;

  private HibernateTemplate hibernateTemplate;

  // Do this to avoid circular dependency between ChangeTrackingListener and
  // Hibernate SessionFactory

  private HibernateTemplate getHibernateTemplate() {
    if (hibernateTemplate == null) {
      hibernateTemplate = ctx.getBeansOfType(HibernateTemplate.class).values().iterator().next();
    }
    return hibernateTemplate;
  }

  @Override
  public void add(EntityCrudEvent event) {
    String eventType = event.getType().toString();
    String jsonForEntity = jacksonHelper.toJson(event.getEntity());
    jdbcTemplate.update(
        "INSERT INTO ENTITY_CRUD_EVENT(ID, EVENT_TYPE, entity_id, entity_type, JSON, processed) VALUES(?,?, ?, ?, ?, ?)", null,
        eventType, event.getEntityId(), event.getEntityType(), jsonForEntity, false);
  }

  @Override
  public JsonEntityCrudEvent findNextEvent() {
    DetachedCriteria dc = DetachedCriteria.forClass(JsonEntityCrudEvent.class);
    dc.add(Restrictions.eq("processed", Boolean.FALSE));
    dc.addOrder(Order.asc("id"));
    return (JsonEntityCrudEvent) DataAccessUtils.singleResult(getHibernateTemplate().findByCriteria(dc, 0, 1));
  }

  @Override
  public void deleteAll() {
    getHibernateTemplate().deleteAll(findAll());
  }

  @Override
  public List<JsonEntityCrudEvent> findAll() {
    DetachedCriteria dc = DetachedCriteria.forClass(JsonEntityCrudEvent.class);
    dc.addOrder(Order.asc("id"));
    return getHibernateTemplate().findByCriteria(dc);
  }

  @Override
  public List<JsonEntityCrudEvent> findEventsToPublish() {
    DetachedCriteria dc = DetachedCriteria.forClass(JsonEntityCrudEvent.class);
    dc.add(Restrictions.eq("processed", Boolean.FALSE));
    dc.addOrder(Order.asc("id"));
    return getHibernateTemplate().findByCriteria(dc, 0, 1);
  }

  @Override
  public void update(List<JsonEntityCrudEvent> events) {
    for (JsonEntityCrudEvent event : events) {
      getHibernateTemplate().merge(event);
    }
  }

}
