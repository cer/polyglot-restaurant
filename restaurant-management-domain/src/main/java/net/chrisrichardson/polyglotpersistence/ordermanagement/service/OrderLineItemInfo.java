package net.chrisrichardson.polyglotpersistence.ordermanagement.service;

public class OrderLineItemInfo {
  
  private String name;
  private int quantity;

  public OrderLineItemInfo() {
  }

  public OrderLineItemInfo(String name, int quantity) {
    this.name = name;
    this.quantity = quantity;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }
}
