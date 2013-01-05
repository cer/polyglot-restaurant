package net.chrisrichardson.polyglotpersistence.ordermanagement.service;

import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.Order;

public interface OrderManagementService {
  Order createOrder(OrderInfo orderInfo);
}
