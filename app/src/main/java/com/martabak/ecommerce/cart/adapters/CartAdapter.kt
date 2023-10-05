package com.martabak.ecommerce.cart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.martabak.ecommerce.cart.CartViewModel
import com.martabak.core.database.CartEntity
import com.martabak.ecommerce.databinding.CartItemBinding

class CartAdapter(private val viewModel: CartViewModel, private val onClick : (String) -> Unit) : ListAdapter<CartEntity, CartViewHolder>(CartComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding, viewModel, parent.context, onClick)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
