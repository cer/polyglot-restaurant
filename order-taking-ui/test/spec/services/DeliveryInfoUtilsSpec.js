describe('DeliveryInfoUtils', function() {
 
  var dayOfWeek = new Date().getDay();
  
  beforeEach(module('deliveryinfoutils'));

  describe('DeliveryInfoUtils.makeDeliveryInfo', function() {
 
    var makeDeliveryInfo;

    beforeEach(inject(function(DeliveryInfoUtils) {
      makeDeliveryInfo = DeliveryInfoUtils.makeDeliveryInfo;
    }));

    it('should handle 12am', function() {
      expect(makeDeliveryInfo({hour: 12, minute: 33, ampm: "am"}, "94619")).
        toEqual({ address : { zipcode : '94619' }, time: {dayOfWeek : dayOfWeek, hour: 0, minute: 33 }, timeOfDay : '12:33 am' });
    });
 
    it('should handle 11am', function() {
      expect(makeDeliveryInfo({hour: 11, minute: 44, ampm: "am"}, "94619")).
        toEqual({ address : { zipcode : '94619' }, time: {dayOfWeek : dayOfWeek, hour: 11, minute: 44 }, timeOfDay : '11:44 am' });
    });

    it('should handle pm', function() {
      expect(makeDeliveryInfo({hour: 12, minute: 33, ampm: "pm"}, "94619")).
        toEqual({ address : { zipcode : '94619' }, time: {dayOfWeek : dayOfWeek, hour: 12, minute: 33 }, timeOfDay : '12:33 pm' });
      expect(makeDeliveryInfo({hour: 11, minute: 44, ampm: "pm"}, "94619")).
        toEqual({ address : { zipcode : '94619' }, time: {dayOfWeek : dayOfWeek, hour: 23, minute: 44 }, timeOfDay : '11:44 pm' });
    });

   });

});

