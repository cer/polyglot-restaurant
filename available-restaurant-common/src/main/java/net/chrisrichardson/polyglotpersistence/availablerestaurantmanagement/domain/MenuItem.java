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


public class MenuItem {

    private int id = -1;

    private int version;

    private String name;

    private double price;

    public MenuItem() {
    }

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    // Extra setters
    
    public void setId(int id) {
      this.id = id;
    }

    public void setVersion(int version) {
      this.version = version;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setPrice(double price) {
      this.price = price;
    }


}