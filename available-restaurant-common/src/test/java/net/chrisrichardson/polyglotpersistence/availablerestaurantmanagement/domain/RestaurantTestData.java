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

import java.util.Calendar;
import java.util.Date;

import net.chrisrichardson.polyglotpersistence.util.Address;

public class RestaurantTestData {

    public static Date makeGoodDeliveryTime() {
        return getTimeTomorrow(20);
    }

    public static Date getTimeTomorrow(int hour) {
        return getTimeTomorrow(hour, 0);
    }

    public static Date getTimeTomorrow(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
            c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.clear(Calendar.MILLISECOND);
        Date during = c.getTime();
        return during;
    }

    public static Address getADDRESS1() {
        return new Address("1 somewhere", null, "Oakland", "CA",
                ZIP1);
    }

    public static Address getADDRESS2() {
        return new Address("22 somewhere else", null, "Oakland", "CA",
                ZIP2);
    }

    public static Address getBAD_ADDRESS() {
        return new Address("1 somewhere", null, "Oakland", "CA",
                BAD_ZIP);
    }

    public static String ZIP1 = "94619";
    public static String ZIP2 = "94618";

    public static String BAD_ZIP = "45001";

    public static final String GOOD_ZIP_CODE = "94619";

    public static final String BAD_ZIP_CODE = "94618";

    public static Date makeBadDeliveryTime() {
      return getTimeTomorrow(4);
    }

	public static Date makeDeliveryTime(int dayOfWeek, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.clear(Calendar.MILLISECOND);
        if (c.getTime().before(new Date()))
        	c.add(Calendar.DAY_OF_MONTH, 7);
        System.out.println("deliveryTime=" + c.getTime());
        return c.getTime();
	}

}