package com.jeff.pizzas.ui.details

import androidx.lifecycle.*
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PizzaDetailsViewModel @Inject constructor(
    private var repository: JeffRepository
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _request.value = Event.error(throwable.message.toString(), null)
    }

    private val _id = MutableLiveData<Int>()

    private val _pizza = _id.switchMap { id ->
        repository.getPizza(id)
    }

    val pizza = _pizza

    val pendingOrder: LiveData<Event<Order>>
        get() = _pendingOrder
    private var _pendingOrder = MutableLiveData<Event<Order>>()

    val request: LiveData<Event<Order>>
        get() = _request
    private val _request = MutableLiveData<Event<Order>>()

    fun start(id: Int) {
        _id.value = id
        getPendingOrder()
    }

    private fun getPendingOrder() {
        _pendingOrder = repository.getPendingOrder() as MutableLiveData<Event<Order>>
    }

    fun registerOrderLine(order: Order?) {
        _request.value = Event.loading()

        viewModelScope.launch(coroutineExceptionHandler) {
            if (order != null) {
                repository.registerOrder(order)
                _request.value = Event.success(order)
            } else {
                _request.value = Event.error("Error: order not exist")
            }
        }
    }
}