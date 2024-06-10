package com.example.eterationtestproject.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.eterationtestproject.models.CartEntity
import com.example.eterationtestproject.query.CartDao
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
class CartRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartRepository: CartRepository
    private val cartDao: CartDao = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        cartRepository = CartRepository(cartDao)
        every { cartDao.getAllCartItems() } returns MutableLiveData(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertCartItem_insertsItem() = testScope.runTest {
        val cartItem = CartEntity(1, "Test Item", 100.0, 1)
        coEvery { cartDao.insert(cartItem) } just Runs
        cartRepository.insert(cartItem)
        coVerify { cartDao.insert(cartItem) }
    }

    @Test
    fun updateCartItem_updatesItem() = testScope.runTest {
        val cartItem = CartEntity(1, "Test Item", 100.0, 1)
        coEvery { cartDao.update(cartItem) } just Runs
        cartRepository.update(cartItem)
        coVerify { cartDao.update(cartItem) }
    }

    @Test
    fun deleteCartItem_deletesItem() = testScope.runTest {
        val cartItem = CartEntity(1, "Test Item", 100.0, 1)
        coEvery { cartDao.delete(cartItem) } just Runs
        cartRepository.delete(cartItem)
        coVerify { cartDao.delete(cartItem) }
    }

    @Test
    fun getAllCartItems_returnsItems() {
        val cartItems = listOf(CartEntity(1, "Test Item", 100.0, 1))
        every { cartDao.getAllCartItems() } returns MutableLiveData(cartItems)
        val liveData = cartRepository.allCartItems
        liveData.observeForever {
            assertEquals(cartItems, it)
        }
    }

    @Test
    fun getCartItemByNameSync_returnsItem() = testScope.runTest {
        val cartItem = CartEntity(1, "Test Item", 100.0, 1)
        coEvery { cartDao.getCartItemByNameSync("Test Item") } returns cartItem
        val result = cartRepository.getCartItemByNameSync("Test Item")
        assertEquals(cartItem, result)
        coVerify { cartDao.getCartItemByNameSync("Test Item") }
    }
}
