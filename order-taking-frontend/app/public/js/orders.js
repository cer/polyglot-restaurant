angular.module('orders', ['ngResource']).
    factory('Orders', function($resource) {
      
      var Orders = $resource('/orders');
      return Orders;
    });