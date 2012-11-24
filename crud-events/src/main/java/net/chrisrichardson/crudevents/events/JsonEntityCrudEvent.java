package net.chrisrichardson.crudevents.events;

public class JsonEntityCrudEvent {

  private int id;
  private String eventType;
  private String json;
  private boolean processed;
  private String entityId;
  private String entityType;

  public JsonEntityCrudEvent() {
  }

  public JsonEntityCrudEvent(int id, String eventType, String entityId, String entityType, String json) {
    super();
    this.id = id;
    this.eventType = eventType;
    this.entityId = entityId;
    this.entityType = entityType;
    this.json = json;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public boolean isProcessed() {
    return processed;
  }

  public void setProcessed(boolean processed) {
    this.processed = processed;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
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
