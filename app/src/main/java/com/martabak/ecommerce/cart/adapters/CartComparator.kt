package com.martabak.ecommerce.cart.adapters

import androidx.recyclerview.widget.DiffUtil
import com.martabak.ecommerce.database.CartEntity

class CartComparator : DiffUtil.ItemCallback<CartEntity>() {
    override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
        return oldItem.item_id == newItem.item_id
    }

    override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
        return oldItem == newItem
    }
}
