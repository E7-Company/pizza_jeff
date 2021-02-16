package com.jeff.pizzas.data.remote

import com.jeff.pizzas.model.Pizza
import retrofit2.Response
import retrofit2.http.GET

interface JeffAPI {

    @GET("raw")
    suspend fun getPizzas(): Response<List<Pizza>>

}