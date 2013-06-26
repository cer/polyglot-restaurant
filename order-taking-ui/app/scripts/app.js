'use strict';

var orderTakingModule = angular.module('ordertaking', ['orderTakingServices', 'orderstate', 'deliveryinfoutils']).
    config(function ($routeProvider) {
        $routeProvider.
            when('/', {controller: 'DeliveryInfoCtrl', templateUrl: 'views/enterdeliveryinformation.html'}).
            when('/displayavailable', {controller: 'DisplayAvailableCtrl', templateUrl: 'views/displayavailable.html'}).
            when('/selectrestaurant/:id', {controller: 'SelectRestaurantCtrl', templateUrl: 'views/displaymenu.html'}).
            when('/ordersummary', {controller: 'DisplayOrderSummaryCtrl', templateUrl: 'views/ordersummary.html'}).
            when('/orderfinalsummary', {controller: 'PlaceOrderCtrl', templateUrl: 'views/orderfinalsummary.html'}).
            when('/orderconfirmation', {controller: 'DisplayOrderConfirmationCtl', templateUrl: 'views/orderconfirmation.html'}).
            when('/aboutus', {templateUrl: 'views/aboutus.html'}).
            when('/help', {templateUrl: 'views/help.html'}).
            when('/howitworks', {templateUrl: 'views/howitworks.html'}).
            otherwise({redirectTo: '/'});
    });
