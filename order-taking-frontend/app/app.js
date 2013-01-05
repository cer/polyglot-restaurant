var express = require('express')
    , request = require('request')
    , cloudfoundry = require("cloudfoundry")
    ;


var app = express.createServer();

app.configure(function() {
    //app.use(express.bodyParser());
    //app.use(express.query());
    app.use(app.router);
    app.use(express.static(__dirname + '/public'));
});

app.configure('development',
function() {
    app.use(express.errorHandler({
        dumpExceptions: true,
        showStack: true
    }));
});

var port = cloudfoundry.port || 3000;

app.listen(port,
	function() {
	    console.log("Express server listening on port %d in %s mode", port, app.settings.env);
	});

function proxyToAvailableRestaurant() {
    return proxyToBackend('http://available-restaurant.cloudfoundry.com');
}

function proxyToOrders() {
    return proxyToBackend('http://restaurant-management.cloudfoundry.com');
}

function proxyToBackend(baseUrl) {
  return function (req, res) {
    console.log("proxyToBackend=", {baseUrl: baseUrl, method: req.method});
    function callback(error, response, body) {
        console.log("error=", error);
    };
    var x = request(baseUrl + req.url, callback);
    req.pipe(x);
    x.pipe(res);
  };
};

app.get('/availablerestaurants', proxyToAvailableRestaurant());
app.get('/restaurant/*', proxyToAvailableRestaurant());
app.post('/orders', proxyToOrders());
app.get('/orders', proxyToOrders());


