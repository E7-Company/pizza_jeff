package com.jeff.pizzas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pizzas")
data class Pizza (
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val content: String,
    val imageUrl: String,
    val prices: List<Price>
)
