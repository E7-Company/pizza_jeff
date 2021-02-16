package com.jeff.pizzas.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.model.OrderLine
import com.jeff.pizzas.model.Pizza
import com.jeff.pizzas.model.User
import com.jeff.pizzas.utils.Constants
import com.jeff.pizzas.utils.Converters

@Database(entities = [User::class, Pizza::class, Order::class, OrderLine::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jeffDao(): JeffDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, Constants.DATABASE)
                .fallbackToDestructiveMigration()
                .build()
    }

}