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

public class TimeRange {
	private int id = -1;

	private int dayOfWeek;

	private int openingTime;
	private int closingTime;
	
	public TimeRange() {
	}

	public TimeRange(int dayOfWeek, int openHour, int openMinute,
			int closeHour, int closeMinute) {
		this(dayOfWeek, openHour * 100 + openMinute, closeHour * 100 + closeMinute);
	}

	public TimeRange(int dayOfWeek, int openingTime, int closingTime) {
    this.dayOfWeek = dayOfWeek;
    this.openingTime = openingTime;
    this.closingTime = closingTime;
  }

  public boolean equals(Object x) {
		if (x == null)
			return false;
		if (!(x instanceof TimeRange))
			return false;

		TimeRange other = (TimeRange) x;

		return dayOfWeek == other.dayOfWeek && openingTime == other.openingTime && closingTime == other.closingTime;
	}

	public int hashCode() {
		return dayOfWeek ^ openingTime ^ closingTime;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public boolean isOpenAtThisTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_WEEK);
		int timeOfDay = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
		return day == getDayOfWeek()
				&& openingTime <= timeOfDay && timeOfDay <= closingTime;
	}

  public int getClosingTime() {
    return closingTime;
  }

  public int getOpeningTime() {
    return openingTime;
  }
  
  public int getOpenHour() {
    return openingTime / 100;
  }
  
  public int getOpenMinute() {
    return openingTime % 100;
  }
  
  public int getCloseHour() {
    return closingTime / 100;
  }
  
  public int getCloseMinute() {
    return closingTime % 100;
  }
  
  /// Extra setters

  public void setId(int id) {
    this.id = id;
  }

  public void setDayOfWeek(int dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public void setOpeningTime(int openingTime) {
    this.openingTime = openingTime;
  }

  public void setClosingTime(int closingTime) {
    this.closingTime = closingTime;
  }
  
  
}