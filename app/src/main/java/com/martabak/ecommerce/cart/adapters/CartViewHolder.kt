package com.martabak.ecommerce.cart.adapters

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.cart.CartViewModel
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.databinding.CartItemBinding

class CartViewHolder(private var binding: CartItemBinding, private var viewModel: CartViewModel) :
    RecyclerView.ViewHolder(binding.root) {
        private var itemId = ""

        fun bind(itemData : CartEntity) {

            binding.cartItemImage.load(itemData.productImage) {
                placeholder(R.drawable.thumbnail)
                error(R.drawable.thumbnail)
            }
            itemId = itemData.item_id
            binding.cartItemName.text = itemData.productName
            binding.cartItemVariant.text = itemData.productVariant
            binding.itemCheckbox.isChecked = itemData.isSelected
        }


}