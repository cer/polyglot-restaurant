package net.chrisrichardson.polyglotpersistence.restaurantmanagement.webapp;

import javax.servlet.http.HttpServletResponse;

import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.service.RestaurantManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class RestaurantCrudController {

  @Autowired
  private RestaurantManagementService restaurantManagementService;

  @RequestMapping(value = "/restaurants", method = RequestMethod.POST)
  public void createOrUpdateRestaurant(@RequestBody Restaurant r, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
    if (r.getId() == 0) {
      restaurantManagementService.add(r);
      String uriString = uriBuilder.path("/restaurants/{id}").buildAndExpand(r.getId()).toUriString();
      response.setHeader("location", uriString);
    } else {
      restaurantManagementService.update(r);
    }
  }

  @RequestMapping(value = "/restaurants/{id}", method = RequestMethod.GET)
  @ResponseBody
  public Restaurant getRestaurant(@PathVariable("id") int id) {
    return restaurantManagementService.findById(id);
  }

}
