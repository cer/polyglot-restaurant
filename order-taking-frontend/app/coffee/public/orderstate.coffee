angular.module('orderstate', []).
    factory('OrderState', -> 
      
      class OrderState

        getMenuItemQuantities: -> mi.quantity for mi in @menuItemsOfSelectedRestaurant()

        menuItemsOfSelectedRestaurant: -> @selectedRestaurant?.menuItems || []
        getSelectedMenuItems: -> mi for mi in @menuItemsOfSelectedRestaurant() when mi.quantity > 0
        
        updateSelectedMenuItems: ->
          @selectedMenuItems = @getSelectedMenuItems()
          @totalCost = 0
          for mi in @selectedMenuItems
             @totalCost = @totalCost + mi.quantity * mi.price

              

        makeOrder: ->
          deliveryInfo: @deliveryInfo
          restaurantId: @selectedRestaurant.id
          orderLineItems: {name: mi.name, quantity: mi.quantity} for mi in @selectedMenuItems
  
      return new OrderState();
    );