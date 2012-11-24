package net.chrisrichardson.crudevents.changetracking.hibernate;

import java.io.Serializable;
import java.util.List;

import net.chrisrichardson.crudevents.events.EntityCrudEvent;
import net.chrisrichardson.crudevents.events.EntityCrudEventType;
import net.chrisrichardson.crudevents.repository.EntityCrudEventRepository;

import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.springframework.beans.factory.annotation.Autowired;

public class ChangeTrackingListener implements PostInsertEventListener, PostDeleteEventListener, PostUpdateEventListener {

  private static final long serialVersionUID = 1L;

  @Autowired
  private EntityCrudEventRepository entityCrudEventRepository;

  private List<Class<?>> trackedEntityClasses;

  public void setTrackedEntityClasses(List<Class<?>> trackedEntityClasses) {
    this.trackedEntityClasses = trackedEntityClasses;
  }

  private boolean isTrackedEntity(Object entity) {
    for (Class<?> type : trackedEntityClasses) {
      if (type.isInstance(entity))
        return true;
    }
    return false;
  }

  private void maybeTrackChange(Serializable id, Object entity, EntityCrudEventType eventType) {
    if (isTrackedEntity(entity)) {
      entityCrudEventRepository.add(new EntityCrudEvent(eventType, id, entity));
    }
  }

  @Override
  public void onPostInsert(PostInsertEvent event) {
    Object entity = event.getEntity();
    maybeTrackChange(event.getId(), entity, EntityCrudEventType.CREATE);
  }

  @Override
  public void onPostUpdate(PostUpdateEvent event) {
    Object entity = event.getEntity();
    maybeTrackChange(event.getId(), entity, EntityCrudEventType.UPDATE);
  }

  @Override
  public void onPostDelete(PostDeleteEvent event) {
    Object entity = event.getEntity();
    maybeTrackChange(event.getId(), entity, EntityCrudEventType.DELETE);
  }

}
