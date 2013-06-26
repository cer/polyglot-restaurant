express = require('express')


app = express.createServer()

app.configure(->
  app.use(app.router)
)

app.configure('development', ->
  app.use(express.errorHandler(
    dumpExceptions: true
    showStack: true
  ))
)

app.listen(3000, ->
  console.log("Express server listening on port %d in %s mode", 3000, app.settings.env))

testRestaurants = [
     {
       name: "Ajanta Restaurant",
       menuItems: [
                    { name: "Samosas", price: 6},
                    { name: "Chicken Vindaloo", price: 13}
       ]
     },
     {
       name: "Sahn Maru",
       menuItems: [
                    { name: "Seafood pancake", price: 15},
                    { name: "Spicy BBQ pork", price: 16}
       ]
     },
     {
       name: "Tamarindo Antojeria",
       menuItems: [
                    { name: "Ceviche", price: 9},
                    { name: "Torta Ahogada", price: 12}
       ]
     }
   ]

for i of testRestaurants
  console.log("i=", i)
  testRestaurants[i].id = i

console.log("testRestaurants=", testRestaurants)

findAvailableRestaurants = (req, res) ->
  res.json({availableRestaurants: ({id: r.id, name: r.name} for r in testRestaurants)});

findRestaurantDetails = (req, res) ->
  res.json(testRestaurants[req.params.id]);

createOrder = (req, res) ->
  res.json({orderNumber: 1});

findOrders = (req, res) ->
  res.json({});

app.get('/app/availablerestaurants', findAvailableRestaurants)
app.get('/app/restaurant/:id', findRestaurantDetails)
app.post('/app/orders', createOrder)
app.get('/app/orders', findOrders)


