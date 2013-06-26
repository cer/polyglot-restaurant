express = require('express')
request = require('request')
cloudfoundry = require("cloudfoundry")


app = express.createServer()

app.configure( -> 
    app.use(app.router)
    app.use(express.static(__dirname + '/../public'))
)

app.configure('development',  -> 
    app.use(express.errorHandler(
        dumpExceptions: true
        showStack: true
    ))
)

port = cloudfoundry.port || 3000

app.listen(port,  -> console.log("Express server listening on port %d in %s mode", port, app.settings.env))

proxyToAvailableRestaurant =  -> proxyToBackend('http://available-restaurant.cloudfoundry.com')

proxyToOrders =  -> proxyToBackend('http://restaurant-management.cloudfoundry.com')


proxyToBackend = (baseUrl) ->
  (req, res) ->
    console.log("proxyToBackend=", {baseUrl: baseUrl, method: req.method})
    callback = (error, response, body) -> console.log("error=", error)
    x = request(baseUrl + req.url.substring("/app".length), callback)
    req.pipe(x)
    x.pipe(res)

app.get('/app/availablerestaurants', proxyToAvailableRestaurant())
app.get('/app/restaurant/*', proxyToAvailableRestaurant())
app.post('/app/orders', proxyToOrders())
app.get('/app/orders', proxyToOrders())


