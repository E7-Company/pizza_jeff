package com.jeff.pizzas

import com.jeff.pizzas.model.User
import com.jeff.pizzas.ui.register.RegisterViewModel
import com.jeff.pizzas.utils.dummy.userDummy
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jeff.pizzas.data.JeffRepository
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
class RegisterViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: JeffRepository

    @Mock
    private lateinit var userObserver: Observer<Event<User>>

    private lateinit var eventGetUser: Event<User>
    private lateinit var liveDataEvent: LiveData<Event<User>>

    @Test
    fun `retrieve user from Repository correctly`() {
        eventGetUser = Event(Event.Status.SUCCESS, userDummy, null)
        liveDataEvent = MutableLiveData(eventGetUser)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getUser()

            val viewModel = RegisterViewModel(repository)
            viewModel.user.observeForever(userObserver)

            verify(repository).getUser()
            verify(userObserver).onChanged(Event.success(userDummy))

            assertEquals(userDummy, viewModel.user.value?.data)

            viewModel.user.removeObserver(userObserver)
        }
    }

    @Test
    fun `error retrieving user from Repository`() {
        val errorMessage = "Error to get user"
        eventGetUser = Event(Event.Status.ERROR, null, errorMessage)
        liveDataEvent = MutableLiveData(eventGetUser)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getUser()

            val viewModel = RegisterViewModel(repository)
            viewModel.user.observeForever(userObserver)

            verify(repository).getUser()
            verify(userObserver).onChanged(Event.error(errorMessage, null))

            assertEquals(errorMessage, viewModel.user.value?.message)

            viewModel.user.removeObserver(userObserver)
        }
    }

    @Test
    fun `register user to Repository correctly when not exist in database`() {
        val errorMessage = "Error to get user"
        eventGetUser = Event(Event.Status.ERROR, null, errorMessage)
        liveDataEvent = MutableLiveData(eventGetUser)

        testCoroutineRule.runBlockingTest {

            doReturn(liveDataEvent)
                .`when`(repository)
                .getUser()

            doReturn(userDummy)
                .`when`(repository)
                .registerUser(userDummy)

            val viewModel = RegisterViewModel(repository)
            viewModel.registerEvent.observeForever(userObserver)
            viewModel.registerUser(userDummy)

            verify(repository).registerUser(userDummy)
            verify(userObserver).onChanged(Event.success(userDummy))

            assertEquals(userDummy, viewModel.registerEvent.value?.data)

            viewModel.registerEvent.removeObserver(userObserver)
        }
    }

}