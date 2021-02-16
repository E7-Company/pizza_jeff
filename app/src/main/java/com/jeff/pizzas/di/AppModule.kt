package com.jeff.pizzas.di

import android.content.Context
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.data.local.AppDatabase
import com.jeff.pizzas.data.local.JeffDao
import com.jeff.pizzas.data.remote.JeffAPI
import com.jeff.pizzas.data.remote.NetworkDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("https://gist.github.com/eliseo-juan/c9c124b0899ae9adc254146783c0b764/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideJeffService(retrofit: Retrofit): JeffAPI = retrofit.create(JeffAPI::class.java)

    @Singleton
    @Provides
    fun provideJeffRemoteDataSource(jeffService: JeffAPI) = NetworkDataSource(jeffService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideJeffDao(db: AppDatabase) = db.jeffDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: NetworkDataSource,
                          localDataSource: JeffDao) =
        JeffRepository(remoteDataSource, localDataSource)
}