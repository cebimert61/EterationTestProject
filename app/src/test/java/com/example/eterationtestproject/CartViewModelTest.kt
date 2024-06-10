package com.example.eterationtestproject.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.eterationtestproject.models.CartEntity
import com.example.eterationtestproject.repository.CartRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class CartViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartViewModel: CartViewModel
    private val cartRepository: CartRepository = mockk(relaxed = true)
    private val application: Application = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)


        every { application.applicationContext } returns application


        cartViewModel = spyk(CartViewModel(application)) {
            every { repository } returns cartRepository
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertCartItem_insertsItem() = testScope.runTest {
        val cartItem = CartEntity(1, "Test Item", 100.0, 1)
        cartViewModel.insert(cartItem)
        coVerify { cartRepository.insert(cartItem) }
    }

    @Test
    fun updateCartItem_updatesItem() = testScope.runTest {
        val cartItem = CartEntity(1, "Test Item", 100.0, 1)
        cartViewModel.update(cartItem)
        coVerify { cartRepository.update(cartItem) }
    }

    @Test
    fun deleteCartItem_deletesItem() = testScope.runTest {
        val cartItem = CartEntity(1, "Test Item", 100.0, 1)
        cartViewModel.delete(cartItem)
        coVerify { cartRepository.delete(cartItem) }
    }

    @Test
    fun getAllCartItems_returnsItems() = testScope.runTest {
        val cartItems = listOf(CartEntity(1, "Test Item", 100.0, 1))
        val liveData = MutableLiveData<List<CartEntity>>()
        liveData.value = cartItems
        every { cartRepository.allCartItems } returns liveData

        val observer: Observer<List<CartEntity>> = spyk(Observer {
            assertEquals(cartItems, it)
        })

        cartViewModel.allCartItems.observeForever(observer)
        cartViewModel.allCartItems.observeForever {
            assertEquals(cartItems, it)
        }
    }
}
