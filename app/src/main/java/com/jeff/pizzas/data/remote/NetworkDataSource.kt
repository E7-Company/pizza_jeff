package com.jeff.pizzas.data.remote

import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val jeffAPI: JeffAPI
): BaseDataSource() {
    suspend fun getPizzas() = getResult { jeffAPI.getPizzas() }
}