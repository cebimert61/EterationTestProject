package com.example.eterationtestproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.eterationtestproject.Repository.ProductRepository
import com.example.eterationtestproject.Response.ProductResponse


class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    fun getProducts(): LiveData<List<ProductResponse>> {
        return repository.getProducts()
    }
}
