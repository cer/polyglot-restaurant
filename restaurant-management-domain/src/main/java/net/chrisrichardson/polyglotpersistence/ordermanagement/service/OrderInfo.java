package net.chrisrichardson.polyglotpersistence.ordermanagement.service;

import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.DeliveryInfo;
import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.PaymentInformation;

import java.util.List;

public class OrderInfo {

  private DeliveryInfo deliveryInfo;
  private int restaurantId;
  private List<OrderLineItemInfo> orderLineItems;
  private PaymentInformation paymentInformation;

  public int getRestaurantId() {
    return restaurantId;
  }

  public DeliveryInfo getDeliveryInfo() {
    return deliveryInfo;
  }

  public List<OrderLineItemInfo> getOrderLineItems() {
    return orderLineItems;
  }

  public PaymentInformation getPaymentInformation() {
    return paymentInformation;
  }

  public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
    this.deliveryInfo = deliveryInfo;
  }

  public void setRestaurantId(int restaurantId) {
    this.restaurantId = restaurantId;
  }

  public void setOrderLineItems(List<OrderLineItemInfo> orderLineItems) {
    this.orderLineItems = orderLineItems;
  }

  public void setPaymentInformation(PaymentInformation paymentInformation) {
    this.paymentInformation = paymentInformation;
  }
}
