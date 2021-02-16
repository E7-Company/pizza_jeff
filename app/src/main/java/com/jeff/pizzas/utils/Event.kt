package com.jeff.pizzas.utils

data class Event<out T>(var status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    var hasBeenHandled = false
        private set

    companion object {
        fun <T> success(data: T): Event<T> {
            return Event(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): Event<T> {
            return Event(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Event<T> {
            return Event(Status.LOADING, data, null)
        }
    }

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }
}