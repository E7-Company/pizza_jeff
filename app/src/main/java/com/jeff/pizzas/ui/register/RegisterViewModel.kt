package com.jeff.pizzas.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.model.User
import com.jeff.pizzas.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: JeffRepository
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _registerEvent.value = Event.error(throwable.message.toString(), null)
    }

    val registerEvent: LiveData<Event<User>>
        get() = _registerEvent
    private val _registerEvent = MutableLiveData<Event<User>>()

    val user: LiveData<Event<User>>
        get() = _user
    private var _user = MutableLiveData<Event<User>>()

    init {
        _user = repository.getUser() as MutableLiveData<Event<User>>
    }

    fun registerUser(user: User) {
        _registerEvent.value = Event.loading()
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.registerUser(user)
            _registerEvent.value = Event.success(user)
        }
    }

}