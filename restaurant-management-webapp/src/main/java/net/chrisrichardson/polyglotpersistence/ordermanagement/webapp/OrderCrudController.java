package net.chrisrichardson.polyglotpersistence.ordermanagement.webapp;

import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.Order;
import net.chrisrichardson.polyglotpersistence.ordermanagement.service.OrderInfo;
import net.chrisrichardson.polyglotpersistence.ordermanagement.service.OrderManagementService;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.service.RestaurantManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/orders")
public class OrderCrudController {

  @Autowired
  private OrderManagementService orderManagementService;

  @RequestMapping(method = RequestMethod.POST)
  public void createOrder(@RequestBody OrderInfo orderInfo, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
    Order order = orderManagementService.createOrder(orderInfo);
    String uriString = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUriString();
    response.setHeader("location", uriString);
  }

}
