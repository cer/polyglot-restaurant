'use strict';

var orderTakingModule = angular.module('ordertaking');

var log = log4javascript.getLogger();
log.addAppender(new log4javascript.BrowserConsoleAppender());

log.debug("hello");

orderTakingModule.controller('DeliveryInfoCtrl',
    function ($scope, $location, AvailableRestaurants, OrderState, DeliveryInfoUtils) {

        $scope.orderState = OrderState;
        $scope.deliveryHours = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        $scope.deliveryMinutes = [0, 15, 30, 45];

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
    });

orderTakingModule.controller('DisplayAvailableCtrl',
    function ($scope, OrderState) {
        $scope.orderState = OrderState;
    });

orderTakingModule.controller('SelectRestaurantCtrl',
    function SelectRestaurantCtrl($scope, $routeParams, OrderState, Restaurant, $location) {

        $scope.orderState = OrderState;

        OrderState.selectedRestaurant = Restaurant.get({id: $routeParams.id});

        console.log("SelectedRestaurant=", OrderState.selectedRestaurant);

        $scope.$watch("orderState.getMenuItemQuantities()", OrderState.noteUpdatedMenuItemQuantities.bind(OrderState), true);

        $scope.displayOrderSummary = function () {
            $location.path('/ordersummary');
        }
    });

orderTakingModule.controller('DisplayOrderSummaryCtrl',
    function DisplayOrderSummaryCtrl($scope, OrderState, $location) {

        $scope.orderState = OrderState;
        $scope.deliveryStates = ["CA"];
        $scope.enterCreditCardNumber = function () {
            $location.path('/orderfinalsummary');
        }
    });

orderTakingModule.controller('PlaceOrderCtrl',
    function PlaceOrderCtrl($scope, $location, OrderState, Orders) {

        $scope.orderState = OrderState;

        $scope.placeOrder = function () {
            var order = OrderState.makeOrder();
            console.log("placing order");
            function success(orderInfo) {
                console.log("orderInfo=", orderInfo);
                OrderState.orderInfo = orderInfo;
                $location.path('/orderconfirmation');
            }

            function error(something) {
                console.log("SomeErrorHappened=", error);
            }

            Orders.save(order, success, error);
        }
    });

orderTakingModule.controller('DisplayOrderConfirmationCtl',
    function DisplayOrderConfirmationCtl($scope) {

    });
