package com.example.eterationtestproject.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eterationtestproject.Response.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {
    private val apiService = ApiClient.apiService

    fun getProducts(): LiveData<List<ProductResponse>> {
        val data = MutableLiveData<List<ProductResponse>>()

        apiService.getProducts().enqueue(object : Callback<List<ProductResponse>> {
            override fun onResponse(
                call: Call<List<ProductResponse>>,
                response: Response<List<ProductResponse>>
            ) {
                if (response.isSuccessful) {
                    data.value = response.body()
                } else {
                    data.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<ProductResponse>>, t: Throwable) {

                data.value = null
            }
        })

        return data
    }
}