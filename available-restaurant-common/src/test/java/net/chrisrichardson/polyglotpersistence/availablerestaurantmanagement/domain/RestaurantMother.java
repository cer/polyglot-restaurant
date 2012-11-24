/*
 * Copyright (c) 2005 Chris Richardson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RestaurantMother {

    private static final String LATE_NIGHT_SNACK = "Late Night Snack";
    public static final String MONTCLAIR_EGGSHOP = "Montclair Eggshop";
	public static final String RESTAURANT_NAME = "Ajanta";
    public static final int OPENING_HOUR = 18;
    public static final int OPENING_MINUTE = 12;
    public static final int CLOSING_MINUTE = 50;
    public static final int CLOSING_HOUR = 22;
    public static final int GOOD_HOUR = 19;
    public static final int BAD_HOUR = 12;
    public static final String SAMOSAS = "Samosas";

    public static Restaurant makeRestaurant() {
        return makeRestaurant("94619");
    }

    public static Restaurant makeRestaurant(String zipCode) {
//        OpeningHours hours = new OpeningHours();
        Set<TimeRange> hours = makeOpeningHours(OPENING_MINUTE);
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        MenuItem mi1 = new MenuItem("Samosas", 5.00);
        MenuItem mi2 = new MenuItem("Chicken Tikka", 6.50);
        menuItems.add(mi1);
        menuItems.add(mi2);

        Set<String> serviceArea = new HashSet<String>();
        serviceArea.add(zipCode);
        serviceArea.add("99999");

        return new Restaurant(RESTAURANT_NAME, "Indian", serviceArea,
                hours, menuItems);
    }

    public static Set<TimeRange> makeOpeningHours(int openMinute) {
      return makeOpeningHours(OPENING_HOUR, openMinute);
    }

    public static Set<TimeRange> makeOpeningHours(int openingHour, int openMinute) {
      Set<TimeRange> hours = new HashSet<TimeRange>();
      
      for (int dayOfWeek : new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY}) {
        hours.add(new TimeRange(dayOfWeek, openingHour, openMinute, CLOSING_HOUR, CLOSING_MINUTE));
        hours.add(new TimeRange(dayOfWeek, 11, 30, 14, 30));
        
      }
      return hours;
    }

    public static Date makeDeliveryTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        c.set(Calendar.HOUR_OF_DAY, 19);
        c.add(Calendar.DAY_OF_MONTH, 7);
        c.clear(Calendar.MILLISECOND);
        c.clear(Calendar.MINUTE);
        return c.getTime();
    }

	public static Restaurant makeEggShopRestaurant() {
		Set<TimeRange> openingHours = new HashSet<TimeRange>();
		for (int dayOfWeek : new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY}) {
			openingHours.add(new TimeRange(dayOfWeek, 7, 0, 14, 30));
		}
		for (int dayOfWeek : new int[]{Calendar.SATURDAY, Calendar.SUNDAY}) {
			openingHours.add(new TimeRange(dayOfWeek, 8, 0, 15, 00));
		}

		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		return new Restaurant(MONTCLAIR_EGGSHOP, "Diner", Collections.singleton("94619"), openingHours, menuItems);
	}

	public static Restaurant makeLateNightTacos() {
	  Set<TimeRange> openingHours = new HashSet<TimeRange>();
	  for (int dayOfWeek : new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY}) {
	    openingHours.add(new TimeRange(dayOfWeek, 22, 0, 23, 0));
	  }

	  List<MenuItem> menuItems = new ArrayList<MenuItem>();
	  return new Restaurant(LATE_NIGHT_SNACK, "Diner", Collections.singleton("94619"), openingHours, menuItems);
	}
}