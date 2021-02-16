package com.jeff.pizzas.utils

object Constants {

    const val DATABASE = "jeff"

    val PIZZASIZES: HashMap<String, Int> = hashMapOf(
        "XL" to 0,
        "L" to 1,
        "M" to 2,
        "S" to 3
    )

    const val MOVIE = "Movie"

    const val USERSTATUS = "USERSTATUS"

    val MARRIEDPIZZAS = listOf("XL", "L")
    val SINGLEPIZZAS = listOf("M", "S")
}