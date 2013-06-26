describe('OrderState', function() {
 
  beforeEach(module('orderstate'));

  var restaurantInfo;

  beforeEach(function () {
    restaurantInfo = {id: 99, name: "Ajanta", menuItems: [{name: "Vegetable Samosas", price: 5.12}, {name: "Chicken Vindaloo", price: 12.34}]};
  })

  // FIXME Put somewhere useful.


  describe('OrderState.getMenuItemQuantities', function() {
 
    var orderState;

    beforeEach(inject(function(OrderState) {
      orderState = OrderState;
    }));

    it('should be [] when no restaurant', function() {
      expect(orderState.getMenuItemQuantities()).toEqual([]);
    });
 
    it('should return undefined quantities', function() {
      orderState.selectedRestaurant = restaurantInfo;
      expect(orderState.getMenuItemQuantities()).toEqual([undefined, undefined]);
    });

    it('should return menuItem quantities', function() {
      orderState.selectedRestaurant = restaurantInfo;
      restaurantInfo.menuItems[0].quantity = 12;
      restaurantInfo.menuItems[1].quantity = 14;
      expect(orderState.getMenuItemQuantities()).toEqual([12, 14]);
    });
   });

  describe('OrderState.noteUpdatedMenuItemQuantities', function() {
 
    var orderState;

    beforeEach(inject(function(OrderState) {
      orderState = OrderState;
    }));

    it('should be undefined when no restaurant', function() {
      orderState.noteUpdatedMenuItemQuantities();
      expect(orderState.selectedMenuItems).toEqual([]);
    });
 
    it('should return undefined quantities', function() {
      orderState.selectedRestaurant = restaurantInfo;
      orderState.noteUpdatedMenuItemQuantities();
      expect(orderState.selectedMenuItems).toEqual([]);
    });

    it('should return menuItem quantities', function() {
      orderState.selectedRestaurant = restaurantInfo;
      restaurantInfo.menuItems[0].quantity = 0;
      restaurantInfo.menuItems[1].quantity = 14;
      orderState.noteUpdatedMenuItemQuantities();
      expect(orderState.selectedMenuItems).toEqual([restaurantInfo.menuItems[1]]);
      expect(orderState.totalCost).toEqual(14 * restaurantInfo.menuItems[1].price)
    });
   });

  describe('OrderState.makeOrder', function() {
 
    var orderState;

    beforeEach(inject(function(OrderState) {
      orderState = OrderState;
    }));

    it('should return menuItem quantities', function() {
      orderState.selectedRestaurant = restaurantInfo;
      restaurantInfo.menuItems[0].quantity = 0;
      restaurantInfo.menuItems[1].quantity = 14;
      orderState.noteUpdatedMenuItemQuantities();

       orderState.deliveryInfo = {};

       orderState.deliveryInfo.address = {
                      street1: "1 High Street",
                      city: "Oakland",
                      state: "CA",
                      zip: "94619"
                    };

      var deliveryTime = {dayOfWeek: new Date().getDay(), hour: 18, minute: 15};
      
      orderState.deliveryInfo.time = deliveryTime;

      var expectedOrder = { deliveryInfo : {
                    time: deliveryTime,
                    address: {
                      street1: "1 High Street",
                      city: "Oakland",
                      state: "CA",
                      zip: "94619"
                    }
                }, 
                restaurantId : restaurantInfo.id, 
                orderLineItems : [ { name : 'Chicken Vindaloo', quantity : 14 } ] };

      expect(orderState.makeOrder()).toEqual(expectedOrder);
    });
   });


});

