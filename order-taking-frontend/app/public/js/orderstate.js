angular.module('orderstate', []).
    factory('OrderState', function() {
      
      function OrderState() {

      };

      OrderState.prototype.updateSelectedMenuItems = function () {
      	var self = this;
      	this.selectedMenuItems = (this.selectedRestaurant && 
        								this.selectedRestaurant.menuItems.filter(function (mi) {return mi.quantity > 0;})) || [];
      	this.totalCost = 0;
      	this.selectedMenuItems.forEach(function (mi) { self.totalCost = self.totalCost + mi.quantity * mi.price});
      }

      OrderState.prototype.getMenuItemQuantities = function () {
 	     return this.selectedRestaurant && 
 	     			this.selectedRestaurant.menuItems && 
 	     			this.selectedRestaurant.menuItems.map(function (mi) {return mi.quantity});
      }

      OrderState.prototype.makeOrder = function () {
       function makeMenuItem(mi) {
        return {name: mi.name, quantity: mi.quantity};
       }
       return {deliveryInfo: this.deliveryInfo,
                restaurantId: this.selectedRestaurant.id, 
                orderLineItems: this.selectedMenuItems.map(makeMenuItem)};
      }

      return new OrderState();
    });