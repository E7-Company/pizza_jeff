package com.jeff.pizzas.utils

import androidx.room.TypeConverter
import com.jeff.pizzas.model.*
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toUserStatus(value: String) = enumValueOf<UserStatus>(value)

    @TypeConverter
    fun fromUserStatus(value: UserStatus) = value.name

    @TypeConverter
    fun pizzaToJson(value: List<Pizza>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToPizza(value: String) = Gson().fromJson(value, Array<Pizza>::class.java).toList()

    @TypeConverter
    fun pricesToJson(value: List<Price>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToPrices(value: String) = Gson().fromJson(value, Array<Price>::class.java).toList()

    @TypeConverter
    fun priceToJson(value: Price?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToPrice(value: String): Price = Gson().fromJson(value, Price::class.java)

    @TypeConverter
    fun orderToJson(value: List<Order>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToOrder(value: String) = Gson().fromJson(value, Array<Order>::class.java).toMutableList()

    @TypeConverter
    fun orderLineToJson(value: List<OrderLine>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToOrderLine(value: String) = Gson().fromJson(value, Array<OrderLine>::class.java).toMutableList()

    @TypeConverter
    fun toOrderStatus(value: String) = enumValueOf<OrderStatus>(value)

    @TypeConverter
    fun fromOrderStatus(value: OrderStatus) = value.name

}