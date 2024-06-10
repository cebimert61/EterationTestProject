package com.example.eterationtestproject.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.eterationtestproject.models.CartDatabase
import com.example.eterationtestproject.models.CartEntity
import com.example.eterationtestproject.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : AndroidViewModel(application) {
    val repository: CartRepository
    val allCartItems: LiveData<List<CartEntity>>
    val cartItemCount = MediatorLiveData<Int>()

    init {
        val cartDao = CartDatabase.getDatabase(application).cartDao()
        repository = CartRepository(cartDao)
        allCartItems = repository.allCartItems

        cartItemCount.addSource(allCartItems) { items ->
            cartItemCount.value = items.sumOf { it.quantity }
        }
    }

    fun insert(cart: CartEntity) = viewModelScope.launch {
        repository.insert(cart)
    }

    fun update(cart: CartEntity) = viewModelScope.launch {
        repository.update(cart)
    }

    fun delete(cart: CartEntity) = viewModelScope.launch {
        repository.delete(cart)
    }

    suspend fun getCartItemByNameSync(name: String): CartEntity? {
        return withContext(Dispatchers.IO) {
            repository.getCartItemByNameSync(name)
        }
    }
}