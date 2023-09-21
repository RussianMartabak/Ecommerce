package com.martabak.ecommerce.cart.adapters

import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.martabak.ecommerce.R
import com.martabak.ecommerce.cart.CartViewModel
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.databinding.CartItemBinding
import java.text.NumberFormat

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
            binding.orderQtyText.text = itemData.productQuantity.toString()
            binding.cartItemStock.text = "Stok ${itemData.productStock}"
            val price = NumberFormat.getInstance().format(itemData.productPrice).replace(",", ".")
            binding.cartItemPrice.text = "Rp$price"
            binding.itemCheckbox.setOnClickListener {
                viewModel.selectItem(itemId, binding.itemCheckbox.isChecked)
            }
            binding.minusButton.setOnClickListener {
                if (itemData.productQuantity == 1) {
                    viewModel.deleteItem(itemId)
                } else {
                    viewModel.substractItem(itemId)
                }
            }
            binding.plusButton.setOnClickListener {
                viewModel.addItem(itemId)
            }
            binding.deleteButton.setOnClickListener {
                viewModel.deleteItem(itemId)
                //log event
                val productBundle = bundleOf("name" to itemData.productName)
                viewModel.analytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART) {
                    param(FirebaseAnalytics.Param.CURRENCY, "IDR")
                    param(FirebaseAnalytics.Param.ITEMS, arrayOf(productBundle))
                }
            }
        }


}