package net.chrisrichardson.polyglotpersistence.util;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

  public static int timeOfDay(Date deliveryTime) {
    Calendar c = toCalendar(deliveryTime);
    return c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
  }

  private static Calendar toCalendar(Date deliveryTime) {
    Calendar c = Calendar.getInstance();
    c.setTime(deliveryTime);
    return c;
  }

  public static int dayOfWeek(Date deliveryTime) {
    Calendar c = toCalendar(deliveryTime);
    return c.get(Calendar.DAY_OF_WEEK);
  }

}
