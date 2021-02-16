package com.jeff.pizzas.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.model.Pizza
import com.jeff.pizzas.model.User

@Dao
interface JeffDao {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * from user")
    fun getUser(): LiveData<User>

    @Update(entity = User::class)
    suspend fun updateUser(order: User?)

    @Query("SELECT * from pizzas ORDER BY name")
    fun getPizzas(): LiveData<List<Pizza>>

    @Query("SELECT * from pizzas WHERE id = :id")
    fun getPizza(id: Int): LiveData<Pizza>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPizzas(pizzas: List<Pizza>)

    @Query("SELECT * from orders WHERE id = :id")
    fun getOrder(id: Int): LiveData<Order>

    @Query("SELECT * from orders WHERE status = 'PENDING'")
    fun getPendingOrder(): LiveData<Order>

    @Update(entity = Order::class)
    suspend fun updateOrder(order: Order?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

}