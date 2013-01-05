package net.chrisrichardson.polyglotpersistence.restaurantmanagement.webapp;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.service.RestaurantManagementService;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;

@Controller
@RequestMapping(value = "/restaurants")
public class RestaurantCrudController {

  @Autowired
  private RestaurantManagementService restaurantManagementService;

  @RequestMapping(method = RequestMethod.POST)
  public void createOrUpdateRestaurant(@RequestBody Restaurant r, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
    if (r.getId() == 0) {
      restaurantManagementService.add(r);
      String uriString = uriBuilder.path("/restaurants/{id}").buildAndExpand(r.getId()).toUriString();
      response.setHeader("location", uriString);
    } else {
      restaurantManagementService.update(r);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  public Restaurant getRestaurant(@PathVariable("id") int id) {
    return restaurantManagementService.findById(id);
  }

}
