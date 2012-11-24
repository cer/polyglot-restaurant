package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.testutil;

import java.lang.reflect.Field;

import net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement.domain.Restaurant;

public class IdentityAssigner {
	private int restaurantId = 1;

	void assignId(Restaurant restaurant) {
		int id = restaurantId++;
		if (id % 1000 == 0)
		  System.out.println("Restaurant.Id=" + id);
		try {
			Field f = getField(Restaurant.class, "id");
			f.setAccessible(true);
			f.set(restaurant, id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	Field getField(Class type, String name) {
		for (Field field : type.getDeclaredFields()) {
			if (field.getName().equals(name))
				return field;
		}
		throw new RuntimeException("Not found: " + name);
	}

	public void assignIdentities(Restaurant... restaurants) {
		for (Restaurant restaurant : restaurants) {
			assignId(restaurant);
		}
	}
}