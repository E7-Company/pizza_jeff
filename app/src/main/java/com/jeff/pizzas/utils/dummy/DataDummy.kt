package com.jeff.pizzas.utils.dummy

import com.jeff.pizzas.model.*

val priceDummy1 = Price(
    "S",
    9.8
)

val priceDummy2 = Price(
    "XL",
    10.0
)

val pizzaDummy1 = Pizza(
    id = 1,
    name = "Tobus Quickwhistle",
    imageUrl = "http://www.publicdomainpictures.net/pictures/10000/nahled/thinking-monkey-11282237747K8xB.jpg",
    content = "Black",
    prices = listOf(priceDummy1)
)

val pizzaDummy2 = Pizza(
    id = 2,
    name = "Malbin Chromerocket",
    imageUrl = "http://www.publicdomainpictures.net/pictures/20000/nahled/snowy-owl-11294429170yBc.jpg",
    content = "Red",
    prices = listOf(priceDummy2)
)

val pizzaDummy3 = Pizza(
    id = 3,
    name = "Minabonk Voidslicer",
    imageUrl = "http://www.publicdomainpictures.net/pictures/20000/nahled/squirrel-in-winter-11298746828jAB.jpg",
    content = "White",
    prices = listOf(priceDummy1)
)

val pizzaDummyList = listOf(pizzaDummy1, pizzaDummy2,pizzaDummy3)

val userDummy = User(
    id = 1,
    name = "Enrique",
    status = UserStatus.MARRIED,
    email = "enenva@gmail.com",
    orders = mutableListOf()
)

val userDummy1 = User(
    id = 1,
    name = "Prueba",
    status = UserStatus.SINGLE,
    email = "prueba@gmail.com",
    orders = mutableListOf()
)

val orderLineDummy1 = OrderLine(
    id = 1,
    title = "Order 1",
    price = priceDummy1
)

val orderLineDummy2 = OrderLine(
    id = 2,
    title = "Order 2",
    price = priceDummy2
)

val orderDummy1 = Order(
    id = 1,
    lines = mutableListOf(),
    time = 111111111,
    status = OrderStatus.PENDING
)

val orderDummy2 = Order(
    id = 1,
    lines = mutableListOf(orderLineDummy2),
    time = 111111111,
    status = OrderStatus.COMPLETE
)