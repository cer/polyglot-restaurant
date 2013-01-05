package net.chrisrichardson.polyglotpersistence.ordermanagement.service;

import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.Order;
import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.OrderLineItem;
import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.OrderRepository;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.MenuItem;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderManagementServiceImpl implements OrderManagementService {

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Override
  public Order createOrder(OrderInfo orderInfo) {
    Restaurant restaurant = restaurantRepository.findRestaurant(orderInfo.getRestaurantId());
    Assert.notNull(restaurant);
    Order order = new Order(orderInfo.getDeliveryInfo(), restaurant, makeLineItems(restaurant, orderInfo.getOrderLineItems()), orderInfo.getPaymentInformation());
    // FIXME - Validate order
    orderRepository.add(order);
    return order;
  }

  private List<OrderLineItem> makeLineItems(Restaurant restaurant, List<OrderLineItemInfo> lineItems) {
    List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>();
    for (OrderLineItemInfo lii : lineItems)
      orderLineItems.add(new OrderLineItem(findMenuItem(restaurant, lii.getName()), lii.getQuantity()));
    return orderLineItems;
  }

  private MenuItem findMenuItem(Restaurant restaurant, String name) {
    for (MenuItem mi : restaurant.getMenuItems())
      if (mi.getName().equals(name))
        return mi;
    throw new RuntimeException("menuItem not found: " + name);

  }
}
