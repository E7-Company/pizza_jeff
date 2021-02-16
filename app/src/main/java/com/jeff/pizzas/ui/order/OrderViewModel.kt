package com.jeff.pizzas.ui.order

import androidx.lifecycle.*
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.model.OrderLine
import com.jeff.pizzas.model.OrderStatus
import com.jeff.pizzas.model.User
import com.jeff.pizzas.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: JeffRepository
) : ViewModel() {

    private val coroutineExceptionOrderHandler = CoroutineExceptionHandler { _, throwable ->
        _updateOrder.value = Event.error(throwable.message.toString(), null)
    }

    private val coroutineExceptionUserHandler = CoroutineExceptionHandler { _, throwable ->
        _updateUser.value = Event.error(throwable.message.toString(), null)
    }

    private val _id = MutableLiveData<Int>()

    private var _order: MutableLiveData<Event<Order>> = _id.switchMap { id ->
        repository.getOrder(id)
    } as MutableLiveData<Event<Order>>

    val order: LiveData<Event<Order>> = _order

    val updateOrder: LiveData<Event<Order?>>
        get() = _updateOrder
    private val _updateOrder = MutableLiveData<Event<Order?>>()

    val updateUser: LiveData<Event<User?>>
        get() = _updateUser
    private val _updateUser = MutableLiveData<Event<User?>>()

    val user: LiveData<Event<User>>
        get() = _user
    private var _user = MutableLiveData<Event<User>>()

    fun start(id: Int) {
        _user = repository.getUser() as MutableLiveData<Event<User>>
        _id.value = id
    }

    fun updateOrderStatus(status: OrderStatus) {
        _updateOrder.value = Event.loading()
        val orderValue = _order.value?.data
        orderValue?.status = status
        viewModelScope.launch(coroutineExceptionOrderHandler) {
            repository.updateOrder(orderValue)
            _updateOrder.value = Event.success(orderValue)
        }
    }

    fun removeOrderLine(orderLine: OrderLine) {
        val orderValue = _order.value?.data
        orderValue?.lines?.remove(orderLine)
        if (orderValue != null) {
            updateOrder(orderValue)
        }
    }

    fun addOrderLine(orderLine: OrderLine) {
        val orderValue = _order.value?.data
        orderValue?.lines?.add(orderLine)
        if (orderValue != null) {
            updateOrder(orderValue)
        }
    }

    private fun updateOrder(order: Order) {
        viewModelScope.launch {
            repository.updateOrder(order)
            _order.value = Event.success(order)
        }
    }

    fun updateUser(user: User) {
        _updateUser.value = Event.loading()
        viewModelScope.launch(coroutineExceptionUserHandler) {
            repository.updateUser(user)
            _updateUser.value = Event.success(user)
        }
    }

}