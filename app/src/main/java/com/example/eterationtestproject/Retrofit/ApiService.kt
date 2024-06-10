package com.example.eterationtestproject.Retrofit

import com.example.eterationtestproject.Response.ProductResponse
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<ProductResponse>>
}