angular.module('deliveryinfoutils', []).
    factory('DeliveryInfoUtils', function () {

        function to24Hour(deliveryTime) {
            return { hour: (deliveryTime.hour == 12 ? 0 : deliveryTime.hour) + (deliveryTime.ampm == "am" ? 0 : 12),
                minute: deliveryTime.minute}
        }

        function toTimeOfDay(deliveryTime) {
            return deliveryTime.hour + ":" + deliveryTime.minute + " " + deliveryTime.ampm;
        }

        function makeTimeOfDay(base, deliveryTime) {
            var t24 = to24Hour(deliveryTime);
            return base * t24.hour + t24.minute;
        };

        function isDeliveryTimeNotInFuture(deliveryTime) {
            // FIXME - This makes the assumption that the browser timezone = zipcode Timezone.
            if (deliveryTime && deliveryTime.hour != undefined && deliveryTime.minute != undefined && deliveryTime.ampm) {
                var now = new Date();
                var timeOfDay = makeTimeOfDay(60, deliveryTime);
                var delta = (timeOfDay - (now.getHours() * 60 + now.getMinutes()));
                console.log("delta=", delta);
                return delta < 60;
            } else {
                console.log("false");
                return false;
            }
        };

        function makeDeliveryInfo(deliveryTime, deliveryZipCode) {
            console.log("deliveryTime=", deliveryTime);

            var t24 = to24Hour(deliveryTime);
            // FIXME - This makes the assumption that the browser timezone = zipcode Timezone.
            var dayOfWeek = new Date().getDay();
            t24.dayOfWeek = dayOfWeek;

            return {address: {zipcode: deliveryZipCode}, time: t24, timeOfDay: toTimeOfDay(deliveryTime)};
        }


        return {
            isDeliveryTimeNotInFuture: isDeliveryTimeNotInFuture,
            makeDeliveryInfo: makeDeliveryInfo
        }
    });