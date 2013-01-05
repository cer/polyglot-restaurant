
angular.module('ordertaking', ['availablerestaurants', 'orderstate', 'restaurant', 'orders', 'deliveryinfoutils']) .
  config(function($routeProvider) {
    $routeProvider.
      when('/', {controller: DeliveryInfoCtrl, templateUrl:'partials/enterdeliveryinformation.html'}).
      when('/displayavailable', {controller: DisplayAvailableCtrl, templateUrl:'partials/displayavailable.html'}).
      when('/selectrestaurant/:id', {controller: SelectRestaurantCtrl, templateUrl:'partials/displaymenu.html'}).
      when('/ordersummary', {controller: DisplayOrderSummaryCtrl, templateUrl:'partials/ordersummary.html'}).
      when('/orderfinalsummary', {controller: PlaceOrderCtrl, templateUrl:'partials/orderfinalsummary.html'}).
      when('/orderconfirmation', {controller: DisplayOrderConfirmationCtl, templateUrl:'partials/orderconfirmation.html'}).
      when('/aboutus', {templateUrl:'partials/aboutus.html'}).
      when('/help', {templateUrl:'partials/help.html'}).
      when('/howitworks', {templateUrl:'partials/howitworks.html'}).
      otherwise({redirectTo:'/'}); 
  }) ;

var log = log4javascript.getDefaultLogger();

log.debug("hello");

function DeliveryInfoCtrl($scope, $location, AvailableRestaurants, OrderState, DeliveryInfoUtils) {
  $scope.orderState = OrderState;
  $scope.deliveryHours = [1,2,3,4,5,6,7,8,9,10,11,12];
  $scope.deliveryMinutes = [0,15,30,45];

  var now = new Date();
  $scope.currentTime = now.getHours() * 100 + now.getMinutes();

  $scope.isNotInFuture = function () {
    return DeliveryInfoUtils.isDeliveryTimeNotInFuture($scope.deliveryTime);
  }

  $scope.showAvailableRestaurants = function () {

    var deliveryInfo = DeliveryInfoUtils.makeDeliveryInfo($scope.deliveryTime, $scope.deliveryZipCode);

    log.debug("deliveryInfo=", deliveryInfo);
  	OrderState.deliveryInfo = deliveryInfo;

  	AvailableRestaurants.get({zipcode: deliveryInfo.address.zipcode, 
        dayOfWeek: deliveryInfo.time.dayOfWeek, hour: deliveryInfo.time.hour, minute: deliveryInfo.time.minute}, 
  		function (ars) {
        console.log("ars=", ars);
  			OrderState.availableRestaurants = ars.availableRestaurants;
  			$location.path('/displayavailable');
  		});

  };
}

function DisplayAvailableCtrl($scope, AvailableRestaurants, OrderState) {
	$scope.orderState = OrderState;
};

function SelectRestaurantCtrl($scope, $routeParams, OrderState, Restaurant, $location) {

    $scope.orderState = OrderState;

    Restaurant.get({id: $routeParams.id}, 
      function (restaurant) {
        OrderState.selectedRestaurant = restaurant;
      });

    $scope.$watch("getMenuItemQuantities()", handleQuantityChanges, true);

    function handleQuantityChanges(oldValue, newValue, scope) {
      OrderState.updateSelectedMenuItems();
    };

    $scope.getMenuItemQuantities = function () {
      return OrderState.getMenuItemQuantities();
    }

    $scope.displayOrderSummary = function () {
      $location.path('/ordersummary');
    }
};

function DisplayOrderSummaryCtrl($scope, OrderState, $location) {
  $scope.orderState = OrderState;
  $scope.deliveryStates = ["CA"];
  $scope.enterCreditCardNumber = function () {
    $location.path('/orderfinalsummary');
  }
}

function PlaceOrderCtrl($scope, $location, OrderState, Orders) {
  $scope.orderState = OrderState;

  $scope.placeOrder = function () {
    var order = OrderState.makeOrder();
    console.log("placing order");
    function success (orderInfo) {
        console.log("orderInfo=", orderInfo);
        OrderState.orderInfo = orderInfo;
        $location.path('/orderconfirmation');
      }
    function error(something) {
      console.log("SomeErrorHappened=", error);
    }
     Orders.save(order, success, error);
  }
}

function DisplayOrderConfirmationCtl($scope) {

};
