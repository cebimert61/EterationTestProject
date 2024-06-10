package com.example.eterationtestproject.repository

import androidx.lifecycle.LiveData
import com.example.eterationtestproject.models.CartEntity
import com.example.eterationtestproject.query.CartDao

class CartRepository(private val cartDao: CartDao) {

    val allCartItems: LiveData<List<CartEntity>> = cartDao.getAllCartItems()

    suspend fun insert(cart: CartEntity) {
        cartDao.insert(cart)
    }

    suspend fun update(cart: CartEntity) {
        cartDao.update(cart)
    }

    suspend fun delete(cart: CartEntity) {
        cartDao.delete(cart)
    }

    suspend fun getCartItemByNameSync(name: String): CartEntity? {
        return cartDao.getCartItemByNameSync(name)
    }
}
