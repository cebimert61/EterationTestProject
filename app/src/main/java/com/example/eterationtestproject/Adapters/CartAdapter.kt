package com.example.eterationtestproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eterationtestproject.R
import com.example.eterationtestproject.models.CartEntity
import com.example.eterationtestproject.viewmodel.CartViewModel

class CartAdapter(
    private val cartViewModel: CartViewModel
) : ListAdapter<CartEntity, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cartItemName: TextView = view.findViewById(R.id.productName)
        private val cartItemPrice: TextView = view.findViewById(R.id.productPrice)
        private val cartItemQuantity: TextView = view.findViewById(R.id.productQty)
        private val increaseButton: Button = view.findViewById(R.id.increaseQty)
        private val decreaseButton: Button = view.findViewById(R.id.decreaseQty)

        fun bind(cartItem: CartEntity, cartViewModel: CartViewModel, adapter: CartAdapter) {
            cartItemName.text = cartItem.name
            cartItemPrice.text = cartItem.price.toString() + " ₺"
            cartItemQuantity.text = cartItem.quantity.toString()

            increaseButton.setOnClickListener {
                cartItem.quantity += 1
                cartViewModel.update(cartItem)
                adapter.notifyItemChanged(adapterPosition)
            }

            decreaseButton.setOnClickListener {
                if (cartItem.quantity > 1) {
                    cartItem.quantity -= 1
                    cartViewModel.update(cartItem)
                    adapter.notifyItemChanged(adapterPosition)
                } else {
                    cartViewModel.delete(cartItem)
                    adapter.submitList(adapter.currentList.toMutableList().apply {
                        removeAt(adapterPosition)
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position), cartViewModel, this)
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartEntity>() {
        override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem == newItem
        }
    }
}
