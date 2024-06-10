package com.example.eterationtestproject.Response

import com.google.gson.annotations.SerializedName

import java.io.Serializable

data class ProductResponse(
    val id: Int,
    val name: String,
    val price: Double,
    val image: String,
    val description: String,
    var quantity: Int = 1
) : Serializable