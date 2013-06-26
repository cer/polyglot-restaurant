Polyglot restaurant example
===========================

This is the code example for my presentation (at JavaOne and elsewhere) on "Developing polyglot persistence applications". Here are the slides: http://www.slideshare.net/chris.e.richardson/developing-polyglot-persistence-applications-javaone-2012  - Please read that presentation first in order for the rest of this README to make sense.

This system has a modular, polyglot application as described here http://plainoldobjects.com/presentations/decomposing-applications-for-deployability-and-scalability/ 

It consists of several different applications (i.e. services) that are independently deployable.

order-taking-frontend
---------------------

A front-end server (a.k.a. API gateway) implemented using NodeJS: serves the browser app HTML/JS/CSS and proxies WS requests to the appropriate backend server

order-taking-ui
---------------

The HTML/JS/CSS browser application implemented using AngularJS and Bootstrap. 

restaurant-management-webapp
----------------------------

This web application exposes a really simple/hacked together RESTful WS for performing CRUD operations on Restaurants. See RestaurantCrudController
For now, this web application also implements OrderManagement web services and backend. See OrderController.

The important part (as it relates to the presentation) is that creating or updating Restaurants adds CRUD events to the queue implemented by a database table.

restaurant-event-publisher
--------------------------

This standalone application processes events in the queue publishing to RabbitMQ

available-restaurant-management
-------------------------------

This is a standalone application that subscribes to RabbitMQ messages updates the materialized view of the restaurant data stored in Redis


available-restaurant-webapp
---------------------------

This web application exposes a really simple RESTful WS for finding available restaurants in Redis (See AvailableRestaurantController).


Other modules
-------------

In addition, to the above modules there are also other modules in the project.

  * available-restaurant-common - code that is common to the available-restaurant-webapp and available-restaurant-management
  * TBD
  
