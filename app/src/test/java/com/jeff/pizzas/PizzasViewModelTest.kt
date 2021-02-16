package com.jeff.pizzas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.utils.dummy.pizzaDummyList
import com.jeff.pizzas.model.Pizza
import com.jeff.pizzas.ui.pizzas.PizzasViewModel
import com.jeff.pizzas.utils.Event
import com.jeff.pizzas.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PizzasViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: JeffRepository

    @Mock
    private lateinit var pizzasObserver: Observer<Event<List<Pizza>>>

    private lateinit var eventPizzas: Event<List<Pizza>>
    private lateinit var liveDataEvent: LiveData<Event<List<Pizza>>>

    @Test
    fun `retrieve all pizzas from Repository correctly`() {
        eventPizzas = Event(Event.Status.SUCCESS, pizzaDummyList, null)
        liveDataEvent = MutableLiveData(eventPizzas)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getPizzas()

            val viewModel = PizzasViewModel(repository)
            viewModel.pizzaList.observeForever(pizzasObserver)

            verify(repository).getPizzas()
            verify(pizzasObserver).onChanged(Event.success(pizzaDummyList))

            assertEquals(pizzaDummyList, viewModel.pizzaList.value?.data)

            viewModel.pizzaList.removeObserver(pizzasObserver)
        }
    }

    @Test
    fun `error retrieving pizzas from Repository`() {
        val errorMessage = "Error loading pizzas"
        eventPizzas = Event(Event.Status.ERROR, null, errorMessage)
        liveDataEvent = MutableLiveData(eventPizzas)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getPizzas()

            val viewModel = PizzasViewModel(repository)
            viewModel.pizzaList.observeForever(pizzasObserver)

            verify(repository).getPizzas()
            verify(pizzasObserver).onChanged(Event.error(errorMessage, null))

            assertEquals(errorMessage, viewModel.pizzaList.value?.message)

            viewModel.pizzaList.removeObserver(pizzasObserver)
        }
    }

}