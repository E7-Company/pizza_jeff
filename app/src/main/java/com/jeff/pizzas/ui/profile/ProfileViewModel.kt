package com.jeff.pizzas.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.model.CompareOrderLineBySize
import com.jeff.pizzas.model.OrderStatus
import com.jeff.pizzas.model.User
import com.jeff.pizzas.utils.Constants
import com.jeff.pizzas.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: JeffRepository
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _updateEvent.value = Event.error(throwable.message.toString(), null)
    }

    val updateEvent: LiveData<Event<User>>
        get() = _updateEvent
    private val _updateEvent = MutableLiveData<Event<User>>()

    val couponEvent: LiveData<Set<String>>
        get() = _couponEvent
    private val _couponEvent = MutableLiveData<Set<String>>()

    val user: LiveData<Event<User>>
        get() = _user
    private var _user = MutableLiveData<Event<User>>()

    init {
        _user = repository.getUser() as MutableLiveData<Event<User>>
    }

    fun updateUser(user: User) {
        _updateEvent.value = Event.loading()
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.updateUser(user)
            _updateEvent.value = Event.success(user)
        }
    }

    fun setCoupon(user: User?) {
        if (user?.orders?.isNotEmpty() == true) {
            val orderComplete = user.orders.lastOrNull { it.status == OrderStatus.COMPLETE }

            orderComplete?.let { order ->
                val orderSizes = order.lines.filter { it.title != Constants.MOVIE }
                    .sortedWith(CompareOrderLineBySize)

                if (orderSizes.isNotEmpty()) {
                    val pizzas = Constants.PIZZASIZES.filter {
                        it.value >= Constants.PIZZASIZES[orderSizes.first().price.size]!!
                    }

                    if (pizzas.isNotEmpty()) {
                        _couponEvent.value = pizzas.keys
                    }
                }
            }

        }
    }
}