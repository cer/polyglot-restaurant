package net.chrisrichardson.polyglotpersistence.availablerestaurant.webapp;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.services.RestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantService;
  
  @RequestMapping(value = "/restaurant/{id}", method = RequestMethod.GET)
  @ResponseBody
  public Restaurant getRestaurant(@PathVariable int id) {
    return restaurantService.findById(id);
  }
}
