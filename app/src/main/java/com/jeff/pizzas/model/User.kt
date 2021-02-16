package com.jeff.pizzas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var email: String,
    var name: String,
    var status: UserStatus,
    var orders: MutableList<Order>
) {

    fun isMarried() = status == UserStatus.MARRIED

}

enum class UserStatus {
    SINGLE,
    MARRIED
}