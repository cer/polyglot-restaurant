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

package net.chrisrichardson.polyglotpersistence.restaurantmanagement.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.RestaurantRepository;
import net.chrisrichardson.polyglotpersistence.util.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateRestaurantRepositoryImpl extends HibernateDaoSupport implements RestaurantRepository {

  @Autowired
  public HibernateRestaurantRepositoryImpl(HibernateTemplate template) {
    setHibernateTemplate(template);
  }

  @Override
  public Restaurant findRestaurant(int restaurantId) {
    Restaurant r = getHibernateTemplate().get(Restaurant.class, restaurantId);
    r.getName();
    r.getMenuItems().size();
    r.getServiceArea().size();
    r.getOpeningHours().size();
    return r;
  }

  public List<Restaurant> findAvailableRestaurants(Address deliveryAddress, Date deliveryTime) {
    String[] paramNames = { "zipCode", "dayOfWeek", "timeOfDay" };
    Object[] paramValues = makeParameterValues(deliveryAddress, deliveryTime);
    return getHibernateTemplate().findByNamedQueryAndNamedParam("findAvailableRestaurants", paramNames, paramValues);
  }

  Object[] makeParameterValues(Address deliveryAddress, Date deliveryTime) {
    Calendar c = Calendar.getInstance();
    c.setTime(deliveryTime);
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    int timeOfDay = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
    String zipCode = deliveryAddress.getZip();

    Object[] values = new Object[] { zipCode, new Integer(dayOfWeek), new Integer(timeOfDay) };
    return values;
  }

  public boolean isRestaurantAvailable(final Address deliveryAddress, final Date deliveryTime) {
    return !findAvailableRestaurants(deliveryAddress, deliveryTime).isEmpty();
  }

  @Override
  public void add(Restaurant restaurant) {
    getHibernateTemplate().save(restaurant);
  }

  @Override
  public void update(Restaurant restaurant) {
    getHibernateTemplate().merge(restaurant);
  }

  @Override
  public void delete(int restaurantId) {
    getHibernateTemplate().delete(getHibernateTemplate().get(Restaurant.class, restaurantId));
  }

}