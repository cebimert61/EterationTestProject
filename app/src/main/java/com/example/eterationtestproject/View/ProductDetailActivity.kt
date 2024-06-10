package com.example.eterationtestproject.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.eterationtestproject.R
import com.example.eterationtestproject.Response.ProductResponse
import com.example.eterationtestproject.databinding.ActivityProductDetailBinding
import com.example.eterationtestproject.viewmodel.CartViewModel
import com.example.eterationtestproject.viewmodel.CartViewModelFactory
import com.example.eterationtestproject.models.CartEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = CartViewModelFactory(application)
        cartViewModel = ViewModelProvider(this, factory).get(CartViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.toolbar.navigationIcon?.setTint(getColor(R.color.white))

        val product = intent.getSerializableExtra("PRODUCT_DETAILS") as? ProductResponse

        product?.let {
            supportActionBar?.title = it.name
            Glide.with(this).load(it.image).into(binding.productImage)
            binding.productName.text = it.name
            binding.productDescription.text = it.description
            binding.productPrice.text = "${it.price} ₺"
        }

        binding.addToCartButton.setOnClickListener {
            product?.let { product ->
                addToCart(product)
            }
        }


        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addToCart(product: ProductResponse) {
        CoroutineScope(Dispatchers.Main).launch {

            cartViewModel.getCartItemByNameSync(product.name)?.let { existingItem ->

                val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
                cartViewModel.update(updatedItem)
            } ?: run {

                val newItem = CartEntity(name = product.name, price = product.price, quantity = 1)
                cartViewModel.insert(newItem)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}
