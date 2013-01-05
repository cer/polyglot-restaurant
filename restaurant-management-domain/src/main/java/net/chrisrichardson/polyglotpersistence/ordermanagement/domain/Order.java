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
 
package net.chrisrichardson.polyglotpersistence.ordermanagement.domain;

import net.chrisrichardson.polyglotpersistence.restaurantmanagement.domain.Restaurant;

import java.util.List;

public class Order {


	private int id;
  private int version;

  private DeliveryInfo deliveryInfo;
  private Restaurant restaurant;
	private List<OrderLineItem> lineItems;
	private PaymentInformation paymentInformation;

	private OrderState state = OrderState.PLACED;

	public Order() {
	}

  public Order(DeliveryInfo deliveryInfo, Restaurant restaurant, List<OrderLineItem> lineItems, PaymentInformation paymentInformation) {
    this.deliveryInfo = deliveryInfo;
    this.restaurant = restaurant;
    this.lineItems = lineItems;
    this.paymentInformation = paymentInformation;
  }

  public int getId() {
    return id;
  }

}