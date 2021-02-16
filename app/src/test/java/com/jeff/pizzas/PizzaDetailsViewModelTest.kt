package com.jeff.pizzas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.model.Pizza
import com.jeff.pizzas.ui.details.PizzaDetailsViewModel
import com.jeff.pizzas.utils.Event
import com.jeff.pizzas.utils.TestCoroutineRule
import com.jeff.pizzas.utils.dummy.*
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PizzaDetailsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: JeffRepository

    @Mock
    private lateinit var pizzaObserver: Observer<Event<Pizza>>

    @Mock
    private lateinit var orderObserver: Observer<Event<Order?>>

    private lateinit var eventPizza: Event<Pizza>
    private lateinit var liveDataEvent: LiveData<Event<Pizza>>

    private lateinit var eventOrder: Event<Order>
    private lateinit var liveDataOrderEvent: LiveData<Event<Order>>

    @Before
    fun setup() {
        eventOrder = Event(Event.Status.SUCCESS, orderDummy1, null)
        liveDataOrderEvent = MutableLiveData(eventOrder)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataOrderEvent)
                .`when`(repository)
                .getPendingOrder()
        }
    }

    @Test
    fun `retrieve pizza details from Repository correctly`() {
        eventPizza = Event(Event.Status.SUCCESS, pizzaDummy1, null)
        liveDataEvent = MutableLiveData(eventPizza)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getPizza(1)

            val viewModel = PizzaDetailsViewModel(repository)
            viewModel.pizza.observeForever(pizzaObserver)
            viewModel.start(1)

            verify(repository).getPizza(1)
            verify(pizzaObserver).onChanged(Event.success(pizzaDummy1))

            assertEquals(pizzaDummy1, viewModel.pizza.value?.data)

            viewModel.pizza.removeObserver(pizzaObserver)
        }
    }

    @Test
    fun `error retrieving pizza details from Repository`() {
        val errorMessage = "Error loading pizza"
        eventPizza = Event(Event.Status.ERROR, null, errorMessage)
        liveDataEvent = MutableLiveData(eventPizza)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getPizza(0)

            val viewModel = PizzaDetailsViewModel(repository)

            viewModel.pizza.observeForever(pizzaObserver)
            viewModel.start(0)

            verify(repository).getPizza(0)
            verify(pizzaObserver).onChanged(Event.error(errorMessage, null))

            assertEquals(errorMessage, viewModel.pizza.value?.message)

            viewModel.pizza.removeObserver(pizzaObserver)
        }
    }

    @Test
    fun `get pending order from Repository correctly`() {
        val viewModel = PizzaDetailsViewModel(repository)

        viewModel.pendingOrder.observeForever(orderObserver)
        viewModel.start(1)

        verify(repository).getPendingOrder()

        assertEquals(orderDummy1, viewModel.pendingOrder.value?.data)

        viewModel.pendingOrder.removeObserver(orderObserver)
    }

    @Test
    fun `register order line correctly`() {
        eventOrder = Event(Event.Status.SUCCESS, orderDummy2, null)
        liveDataOrderEvent = MutableLiveData(eventOrder)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataOrderEvent)
                .`when`(repository)
                .registerOrder(orderDummy2)

            val viewModel = PizzaDetailsViewModel(repository)
            viewModel.request.observeForever(orderObserver)
            viewModel.start(1)
            viewModel.registerOrderLine(orderDummy2)

            verify(repository).registerOrder(orderDummy2)
            verify(orderObserver).onChanged(Event.success(orderDummy2))

            assertEquals(orderDummy2, viewModel.request.value?.data)

            viewModel.request.removeObserver(orderObserver)
        }
    }
}