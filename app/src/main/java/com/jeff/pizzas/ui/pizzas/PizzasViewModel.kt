package com.jeff.pizzas.ui.pizzas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.model.Pizza
import com.jeff.pizzas.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PizzasViewModel @Inject constructor(
    repository: JeffRepository
) : ViewModel() {

    val pizzaList: LiveData<Event<List<Pizza>>>
        get() = _pizzaList
    private var _pizzaList = MutableLiveData<Event<List<Pizza>>>()

    init {
        _pizzaList = repository.getPizzas() as MutableLiveData<Event<List<Pizza>>>
    }

}