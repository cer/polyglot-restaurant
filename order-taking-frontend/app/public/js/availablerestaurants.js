angular.module('availablerestaurants', ['ngResource']).
    factory('AvailableRestaurants', function($resource) {
      
      var AvailableRestaurants = $resource('/availablerestaurants');
 
      return AvailableRestaurants;
    });