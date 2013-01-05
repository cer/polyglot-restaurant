package net.chrisrichardson.polyglotpersistence.availablerestaurant.webapp;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.DeliveryTime;
import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.SimpleAvailableRestaurantRepository;
import net.chrisrichardson.polyglotpersistence.util.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AvailableRestaurantController {

  @Autowired
  private SimpleAvailableRestaurantRepository availableRestaurantRepository;

  @RequestMapping(value = "/availablerestaurants", method = RequestMethod.GET)
  @ResponseBody
  public AvailableRestaurants findAvailableRestaurants(FindAvailableRestaurantsRequest request) {
    // FIXME - we need to do some validation since all request params are valid
    System.out.println("request=" + request);
    Address address = request.makeAddress();
    DeliveryTime deliveryTime = request.makeDeliveryTime();
    return new AvailableRestaurants(availableRestaurantRepository.findAvailableRestaurants(address, deliveryTime));
  }

}
