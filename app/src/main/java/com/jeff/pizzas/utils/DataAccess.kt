package com.jeff.pizzas.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

fun <T, A> performGetOperation(databaseQuery: () -> LiveData<T>,
                               networkCall: suspend () -> Event<A>,
                               saveCallResult: suspend (A) -> Unit): LiveData<Event<T>> =
    liveData(Dispatchers.IO) {
        emit(Event.loading())
        val source = databaseQuery.invoke().map { Event.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Event.Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)

        } else if (responseStatus.status == Event.Status.ERROR) {
            emit(Event.error(responseStatus.message!!))
            emitSource(source)
        }
    }

fun <T> performGetOperation(databaseQuery: () -> LiveData<T>): LiveData<Event<T>> =
    liveData(Dispatchers.IO) {
        emit(Event.loading())
        val source = databaseQuery.invoke().map { Event.success(it) }
        emitSource(source)
    }