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

package net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain;

import java.util.Date;
import java.util.List;

import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;
import net.chrisrichardson.polyglotpersistence.util.Address;

public interface RestaurantRepository {

  public List<Restaurant> findAvailableRestaurants(Address deliveryAddress, Date deliveryTime);

  public boolean isRestaurantAvailable(Address deliveryAddress, Date deliveryDate);

  public Restaurant findRestaurant(int restaurantId);

  public void add(Restaurant restaurant);

  public void update(Restaurant restaurant);

  public void delete(int restaurantId);

}
