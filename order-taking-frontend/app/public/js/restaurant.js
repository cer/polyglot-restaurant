angular.module('restaurant', ['ngResource']).
    factory('Restaurant', function($resource) {
      
      var Restaurant = $resource('/restaurant/:id');
      return Restaurant;
    });