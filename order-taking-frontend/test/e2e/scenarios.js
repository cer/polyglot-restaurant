'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('Order taking application', function() {

  it('should work', function() {
    browser().navigateTo('index.html');
    expect(browser().location().url()).toBe('/');
    input('deliveryZipCode').enter('94619');
    select('deliveryTime.hour').option(6 - 1);
    select('deliveryTime.minute').option("45");
    select('deliveryTime.ampm').option("pm");
    element("#enterDeliveryInfoButton", "enterDeliveryInfoButton").click();

    element("#availableRestaurantsTable a:first", "select available restaurant").click();

    using("#menuTable tr:first", "mi quantity").input("mi.quantity").enter("3");
    expect(element("#orderTotal").text()).toBe("51");
    element("#gotoOrderSummaryButton", "gotoOrderSummary").click()

    input("orderState.name").enter("John Doe");
    input("orderState.creditCardNumber").enter("1234567890");
    
    input("orderState.deliveryInfo.address.street1").enter("1 High Street");
    input("orderState.deliveryInfo.address.city").enter("Oakland");
    select("orderState.deliveryInfo.address.state").option("CA");
    element("#orderSummaryEnterCreditCardNumberButton", "orderSummary.enterCreditCardNumber").click();
    
    element("#orderFinalSummaryPlaceOrderButton", "orderFinalSummaryPlaceOrderButton").click();
    expect(element("#orderNumber").count()).toBe(1);
  });
  
});
