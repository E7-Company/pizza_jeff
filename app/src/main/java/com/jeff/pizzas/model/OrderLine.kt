package com.jeff.pizzas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeff.pizzas.utils.Constants.PIZZASIZES

@Entity(tableName = "orderline")
data class OrderLine (
        @PrimaryKey(autoGenerate = true)
        val id: Int? = 0,
        var title: String,
        var price: Price
)

class CompareOrderLineBySize {
        companion object : Comparator<OrderLine> {
                override fun compare(a: OrderLine, b: OrderLine): Int = when {
                        a.price.size != b.price.size -> PIZZASIZES[a.price.size]!! - PIZZASIZES[b.price.size]!!
                        else -> { 0 }
                }
        }
}