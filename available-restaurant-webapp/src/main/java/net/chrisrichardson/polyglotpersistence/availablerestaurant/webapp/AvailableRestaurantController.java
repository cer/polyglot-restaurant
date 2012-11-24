package net.chrisrichardson.polyglotpersistence.availablerestaurant.webapp;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.SimpleAvailableRestaurantRepository;
import net.chrisrichardson.polyglotpersistence.util.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;

@Controller
public class AvailableRestaurantController {

  @Autowired
  private SimpleAvailableRestaurantRepository availableRestaurantRepository;

  @RequestMapping(value = "/availablerestaurants", method = RequestMethod.GET)
  @ResponseBody
  public AvailableRestaurants getRestaurant(@RequestParam("deliveryZipcode") String zipcode,
      @RequestParam("dayOfWeek") int dayOfWeek, @RequestParam("timeOfDay") int timeOfDay) {
    Address address = new Address(null, null, null, null, zipcode);
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
    c.set(Calendar.HOUR_OF_DAY, timeOfDay / 100);
    c.set(Calendar.MINUTE, timeOfDay % 100);
    Date deliveryTime = c.getTime();
    return new AvailableRestaurants(availableRestaurantRepository.findAvailableRestaurants(address, deliveryTime));
  }

}
