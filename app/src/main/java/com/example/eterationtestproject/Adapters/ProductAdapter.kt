package com.example.eterationtestproject.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eterationtestproject.R
import com.example.eterationtestproject.Response.ProductResponse
import com.example.eterationtestproject.view.ProductDetailActivity

class ProductAdapter(private val addToCartCallback: (ProductResponse) -> Unit) :
    ListAdapter<ProductResponse, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    class ProductViewHolder(view: View, private val addToCartCallback: (ProductResponse) -> Unit) : RecyclerView.ViewHolder(view) {
        private val productImage: ImageView = view.findViewById(R.id.productImage)
        private val productName: TextView = view.findViewById(R.id.productName)
        private val productPrice: TextView = view.findViewById(R.id.productPrice)
        private val addToCartButton: Button = view.findViewById(R.id.addToCartButton)

        fun bind(product: ProductResponse) {
            productName.text = product.name
            productPrice.text = product.price.toString() + " ₺"
            Glide.with(productImage.context).load(product.image).into(productImage)

            addToCartButton.setOnClickListener {
                addToCartCallback(product)
            }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("PRODUCT_DETAILS", product)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view, addToCartCallback)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductResponse>() {
        override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem == newItem
        }
    }
}
