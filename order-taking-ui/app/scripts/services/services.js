angular.module('orderTakingServices', ['ngResource']).
    factory('Restaurant',function ($resource) {
        return $resource('/app/restaurant/:id');
    }).
    factory('Orders',function ($resource) {
        return $resource('/app/orders');
    }).
    factory('AvailableRestaurants', function ($resource) {
        return $resource('/app/availablerestaurants');
    });