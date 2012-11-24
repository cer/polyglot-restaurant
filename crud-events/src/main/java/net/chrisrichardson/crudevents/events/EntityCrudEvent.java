package net.chrisrichardson.crudevents.events;

import java.io.Serializable;

public class EntityCrudEvent {

  private EntityCrudEventType type;
  private String entityId;
  private String entityType;
  private Object entity;

  public EntityCrudEvent() {
  }

  public EntityCrudEvent(EntityCrudEventType type, Serializable entityId, Object entity) {
    this.type = type;
    this.entityId = entityId.toString();
    this.entityType = entity.getClass().getName();
    this.entity = entity;
  }

  public EntityCrudEventType getType() {
    return type;
  }

  public void setType(EntityCrudEventType type) {
    this.type = type;
  }

  public Object getEntity() {
    return entity;
  }

  public void setEntity(Object entity) {
    this.entity = entity;
  }

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public String getEntityType() {
    return entityType;
  }

  public void setEntityType(String entityType) {
    this.entityType = entityType;
  }
}
