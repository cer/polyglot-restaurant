describe('Order taking controllers', function() {
 
  beforeEach(module('availablerestaurants'));
  beforeEach(module('orderstate'));
  beforeEach(module('restaurant'));
  beforeEach(module('orders'));
  beforeEach(module('deliveryinfoutils'));

  var dayOfWeek = new Date().getDay();

  describe('DeliveryInfoCtrl', function(){
 
    var scope, ctrl, $httpBackend, $location, theOrderState;
    var availableRestaurants = {"availableRestaurants":[
            {"name":"tastyfood1354324600850","id":73},
            {"name":"tastyfood1354325202944","id":74},
            {"name":"tastyfood1354325255236","id":75}]};

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, OrderState) {
      $httpBackend = _$httpBackend_;

      scope = $rootScope.$new();
      $location = {path: function(p) { this.newPath = p; }};

      theOrderState = OrderState;
      ctrl = $controller(DeliveryInfoCtrl, {$scope: scope, $location: $location});
    }));

    it('should populate deliveryHours and deliveryMinutes', function() {
      expect(scope.deliveryHours.length).toBe(12);
      expect(scope.deliveryMinutes.length).toBe(4);
    });
    
    it('should fetch available restaurants', function() {
      $httpBackend.expectGET('/availablerestaurants?dayOfWeek=' + dayOfWeek + '&hour=18&minute=15&zipcode=94619').
          respond(availableRestaurants);

      scope.deliveryTime = {hour: 6, minute: 15, ampm: "pm"};
      scope.deliveryZipCode = "94619";
      scope.showAvailableRestaurants();
      
      $httpBackend.flush();

      expect($location.newPath).toBe('/displayavailable');
      expect(theOrderState.availableRestaurants).toEqual(availableRestaurants.availableRestaurants);
    });

  });



 describe('SelectRestaurantCtrl', function(){
    var scope, ctrl, $httpBackend, $location, theOrderState, restaurantInfo;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, OrderState) {
      $httpBackend = _$httpBackend_;

      scope = $rootScope.$new();
      $location = {path: function(p) { this.newPath = p; }};

      theOrderState = OrderState;
      var routeParams = {id: 99};
      restaurantInfo = {name: "Ajanta", menuItems: [{name: "Vegetable Samosas", price: 5.12}, {name: "Chicken Vindaloo", price: 12.34}]};
      $httpBackend.expectGET('/restaurant/99').respond(restaurantInfo);
      ctrl = $controller(SelectRestaurantCtrl, {$scope: scope, $location: $location, $routeParams: routeParams});
      $httpBackend.flush();    
    }));

   it('should populate OrderState with restaurant', function() {
      expect(scope.orderState).toBe(theOrderState);
      
      expect(theOrderState.selectedRestaurant.name).toBe(restaurantInfo.name);
      expect(theOrderState.selectedRestaurant.menuItems).toEqual(restaurantInfo.menuItems);
      expect(theOrderState.selectedMenuItems.length).toBe(0);
      expect(theOrderState.totalCost).toBe(0);
   });

   it('should update selected menu items and total cost', function() {
      scope.orderState.selectedRestaurant.menuItems[0].quantity = 3;

      // FIXME - needed to call $digest() to cause things to update
      scope.$digest();   
      expect(theOrderState.selectedMenuItems.length).toBe(1);
      expect(theOrderState.selectedMenuItems[0].name).toBe(scope.orderState.selectedRestaurant.menuItems[0].name);
      expect(theOrderState.totalCost).toBe(15.36);
   });

   it('should display order summary', function() {
      scope.orderState.selectedRestaurant.menuItems[0].quantity = 3;
      // FIXME - needed to call $digest() to cause things to update
      scope.$digest();   
      scope.displayOrderSummary();
      expect($location.newPath).toBe('/ordersummary');      
   });

 });

 describe('DisplayOrderSummaryCtrl', function(){
    var scope, ctrl, $httpBackend, $location, theOrderState, restaurantInfo;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, OrderState) {
      scope = $rootScope.$new();
      $location = {path: function(p) { this.newPath = p; }};
      theOrderState = OrderState;
      ctrl = $controller(DisplayOrderSummaryCtrl, {$scope: scope, $location: $location});
    }));

   it('should populate scope with OrderState', function() {
      expect(scope.orderState).toBe(theOrderState);
   });

   it('should display final order summary', function() {
      scope.enterCreditCardNumber();
      expect($location.newPath).toBe('/orderfinalsummary');      
   });

 });

 describe('PlaceOrderCtrl', function(){
    var scope, ctrl, $httpBackend, $location, theOrderState, restaurantInfo;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, OrderState) {
      $httpBackend = _$httpBackend_;
      scope = $rootScope.$new();
      $location = {path: function(p) { this.newPath = p; }};
      theOrderState = OrderState;
      ctrl = $controller(PlaceOrderCtrl, {$scope: scope, $location: $location});
    }));

   it('should populate scope with OrderState', function() {
      expect(scope.orderState).toBe(theOrderState);
   });

   it('should create order and display final order summary', function() {
    theOrderState.selectedRestaurant =  {name: "Ajanta", id: 102,
          menuItems: [{name: "Vegetable Samosas", price: 5.12}, {name: "Chicken Vindaloo", price: 12.34}]};
    theOrderState.selectedRestaurant.menuItems[0].quantity = 3;
    theOrderState.updateSelectedMenuItems();

    var orderInfo = {orderId: 101};
    $httpBackend.expectPOST('/orders', {"restaurantId": 102,"orderLineItems":[{"name":"Vegetable Samosas","quantity":3}]}).respond(orderInfo);
    scope.placeOrder();
    $httpBackend.flush();    
    expect(theOrderState.orderInfo.orderId).toEqual(orderInfo.orderId);
    expect($location.newPath).toBe('/orderconfirmation');      
   });

 });

});