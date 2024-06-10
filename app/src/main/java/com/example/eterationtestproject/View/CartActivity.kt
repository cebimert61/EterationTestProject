package com.example.eterationtestproject.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eterationtestproject.adapters.CartAdapter
import com.example.eterationtestproject.R
import com.example.eterationtestproject.databinding.ActivityCartBinding
import com.example.eterationtestproject.viewmodel.CartViewModel
import com.example.eterationtestproject.viewmodel.CartViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.example.eterationtestproject.models.CartEntity

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = CartViewModelFactory(application)
        cartViewModel = ViewModelProvider(this, factory).get(CartViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.navigationIcon?.setTint(getColor(R.color.white))
        supportActionBar?.title = "E-Market"

        cartAdapter = CartAdapter(cartViewModel)
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCart.adapter = cartAdapter

        cartViewModel.allCartItems.observe(this, Observer { cartItems ->
            cartAdapter.submitList(cartItems.toMutableList())
            updateTotalPrice(cartItems)
        })
    }

    private fun updateTotalPrice(cartItems: List<CartEntity>) {
        val total = cartItems.sumByDouble { it.price * it.quantity }
        binding.TotalProductPrice.text = "$total ₺"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
