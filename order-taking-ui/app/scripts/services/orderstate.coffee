angular.module('orderstate', []).
    factory('OrderState', -> 
      
      class OrderState

        menuItemsMaybe: -> @selectedRestaurant?.menuItems || []

        getMenuItemQuantities: -> mi.quantity for mi in @menuItemsMaybe()

        getSelectedMenuItems: -> mi for mi in @menuItemsMaybe() when mi.quantity > 0
        
        noteUpdatedMenuItemQuantities: ->
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