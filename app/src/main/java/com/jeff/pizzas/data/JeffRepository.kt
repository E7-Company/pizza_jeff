package com.jeff.pizzas.data

import com.jeff.pizzas.data.local.JeffDao
import com.jeff.pizzas.data.remote.NetworkDataSource
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.model.User
import com.jeff.pizzas.utils.performGetOperation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JeffRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: JeffDao
) {

    suspend fun registerUser(user: User) {
        localDataSource.insertUser(user)
    }

    fun getUser() = performGetOperation(
        databaseQuery = { localDataSource.getUser() }
    )

    suspend fun updateUser(user: User) {
        localDataSource.updateUser(user)
    }

    fun getPizzas() = performGetOperation(
        databaseQuery = { localDataSource.getPizzas() },
        networkCall = { networkDataSource.getPizzas() },
        saveCallResult = { localDataSource.insertAllPizzas(it) }
    )

    fun getPizza(id: Int) = performGetOperation(
        databaseQuery = { localDataSource.getPizza(id) }
    )

    suspend fun registerOrder(order: Order) {
        localDataSource.insertOrder(order)
    }

    suspend fun updateOrder(order: Order?) {
        localDataSource.updateOrder(order)
    }

    fun getPendingOrder() = performGetOperation(
        databaseQuery = { localDataSource.getPendingOrder() }
    )

    fun getOrder(id: Int) = performGetOperation(
        databaseQuery = { localDataSource.getOrder(id) }
    )

}