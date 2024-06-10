package com.example.eterationtestproject.view

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eterationtestproject.R
import com.example.eterationtestproject.Response.ProductResponse
import com.example.eterationtestproject.viewmodel.ProductViewModel
import com.example.eterationtestproject.viewmodel.CartViewModel
import com.example.eterationtestproject.viewmodel.CartViewModelFactory
import com.example.eterationtestproject.adapters.ProductAdapter
import com.example.eterationtestproject.models.CartEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels { CartViewModelFactory(application) }
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: ProductAdapter
    private lateinit var allProducts: List<ProductResponse>
    private var currentFilter: Int = R.id.filter_all

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.productGrid)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val searchEditText = findViewById<EditText>(R.id.searchBar)
        searchEditText.doOnTextChanged { text, _, _, _ ->
            filterProducts(text.toString())
        }

        productViewModel.getProducts().observe(this, Observer { products ->
            allProducts = products
            adapter = ProductAdapter { product -> addToCart(product) }
            recyclerView.adapter = adapter
            filterProducts(searchEditText.text.toString())
        })

        cartViewModel.allCartItems.observe(this, Observer { cartItems ->

        })

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        setupBottomNavigation()

        cartViewModel.cartItemCount.observe(this, Observer { itemCount ->
            updateCartBadge(itemCount)
        })

        val filterButton = findViewById<Button>(R.id.filterButton)
        filterButton.setOnClickListener {
            val dialog = FilterDialogFragment { filterId ->
                currentFilter = filterId
                filterProducts(searchEditText.text.toString())
            }
            dialog.show(supportFragmentManager, "FilterDialogFragment")
        }
    }

    private fun filterProducts(query: String) {
        val filteredProducts = allProducts.filter {
            it.name.contains(query, ignoreCase = true) && filterByPrice(it)
        }
        adapter.submitList(filteredProducts)
    }

    private fun filterByPrice(product: ProductResponse): Boolean {
        return when (currentFilter) {
            R.id.filter_0_500 -> product.price in 0.0..500.0
            R.id.filter_500_above -> product.price > 500.0
            else -> true
        }
    }

    private fun addToCart(product: ProductResponse) {
        runBlocking {
            val existingCartItem = withContext(Dispatchers.IO) {
                cartViewModel.getCartItemByNameSync(product.name)
            }

            if (existingCartItem != null) {
                existingCartItem.quantity += 1
                cartViewModel.update(existingCartItem)
            } else {
                val cartItem = CartEntity(name = product.name, price = product.price, quantity = 1)
                cartViewModel.insert(cartItem)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.cartItemCount.value?.let { updateCartBadge(it) }
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun updateCartBadge(itemCount: Int = 0) {
        val badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
        badge.isVisible = itemCount > 0
        badge.number = itemCount
    }
}
