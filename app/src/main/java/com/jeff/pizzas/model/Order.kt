package com.jeff.pizzas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var lines: MutableList<OrderLine>,
    var time: Long,
    var status: OrderStatus
)

enum class OrderStatus {
    CANCEL,
    PENDING,
    COMPLETE
}