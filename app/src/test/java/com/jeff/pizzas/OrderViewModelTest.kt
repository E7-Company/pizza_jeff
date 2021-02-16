package com.jeff.pizzas

import com.jeff.pizzas.model.User
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jeff.pizzas.data.JeffRepository
import com.jeff.pizzas.model.Order
import com.jeff.pizzas.model.OrderStatus
import com.jeff.pizzas.ui.order.OrderViewModel
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
class OrderViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: JeffRepository

    @Mock
    private lateinit var orderObserver: Observer<Event<Order?>>

    private lateinit var eventGetUser: Event<User>
    private lateinit var liveDataEvent: LiveData<Event<User>>

    private lateinit var eventOrder: Event<Order>
    private lateinit var liveDataOrderEvent: LiveData<Event<Order>>

    @Before
    fun setup() {
        eventGetUser = Event(Event.Status.SUCCESS, userDummy, null)
        liveDataEvent = MutableLiveData(eventGetUser)

        eventOrder = Event(Event.Status.SUCCESS, orderDummy1, null)
        liveDataOrderEvent = MutableLiveData(eventOrder)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getUser()

            doReturn(liveDataOrderEvent)
                .`when`(repository)
                .getOrder(1)
        }
    }

    @Test
    fun `get order from Repository correctly`() {
        val viewModel = OrderViewModel(repository)
        viewModel.order.observeForever(orderObserver)
        viewModel.start(1)

        verify(repository).getOrder(1)
        verify(orderObserver).onChanged(Event.success(orderDummy1))

        assertEquals(orderDummy1, viewModel.order.value?.data)

        viewModel.order.removeObserver(orderObserver)
    }

    @Test
    fun `change order status correctly`() {
        eventOrder = Event(Event.Status.SUCCESS, orderDummy1, null)
        liveDataOrderEvent = MutableLiveData(eventOrder)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataOrderEvent)
                .`when`(repository)
                .updateOrder(orderDummy1)

            val viewModel = OrderViewModel(repository)
            viewModel.order.observeForever(orderObserver)
            viewModel.start(1)
            viewModel.updateOrderStatus(OrderStatus.COMPLETE)

            verify(repository).updateOrder(orderDummy1)
            verify(orderObserver).onChanged(Event.success(orderDummy1))

            assertEquals(OrderStatus.COMPLETE, viewModel.updateOrder.value?.data?.status)

            viewModel.order.removeObserver(orderObserver)
        }
    }

    @Test
    fun `remove order line correctly`() {
        eventOrder = Event(Event.Status.SUCCESS, orderDummy1, null)
        liveDataOrderEvent = MutableLiveData(eventOrder)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataOrderEvent)
                .`when`(repository)
                .updateOrder(orderDummy1)

            val viewModel = OrderViewModel(repository)
            viewModel.order.observeForever(orderObserver)
            viewModel.start(1)
            viewModel.removeOrderLine(orderLineDummy1)

            verify(repository).updateOrder(orderDummy1)

            assertEquals(0, viewModel.order.value?.data?.lines?.size)

            viewModel.order.removeObserver(orderObserver)
        }
    }

    @Test
    fun `add order line correctly`() {
        eventOrder = Event(Event.Status.SUCCESS, orderDummy1, null)
        liveDataOrderEvent = MutableLiveData(eventOrder)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataOrderEvent)
                .`when`(repository)
                .updateOrder(orderDummy1)

            val viewModel = OrderViewModel(repository)
            viewModel.order.observeForever(orderObserver)
            viewModel.start(1)
            viewModel.addOrderLine(orderLineDummy1)

            verify(repository).updateOrder(orderDummy1)

            assertEquals(orderLineDummy1, viewModel.order.value?.data?.lines?.get(0))

            viewModel.order.removeObserver(orderObserver)
        }
    }

}