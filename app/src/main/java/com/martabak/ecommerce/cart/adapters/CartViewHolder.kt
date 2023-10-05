package com.martabak.ecommerce.cart.adapters

import android.content.Context
import android.util.Log
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.martabak.ecommerce.R
import com.martabak.ecommerce.cart.CartViewModel
import com.martabak.core.database.CartEntity
import com.martabak.ecommerce.databinding.CartItemBinding
import java.text.NumberFormat

class CartViewHolder(private var binding: CartItemBinding, private var viewModel: CartViewModel, private var context : Context, private val onClick : (String) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private var itemId = ""

    fun bind(itemData: CartEntity) {
        binding.cartItemImage.load(itemData.productImage) {
            placeholder(R.drawable.thumbnail)
            error(R.drawable.thumbnail)
        }

        itemId = itemData.item_id
        binding.cartItemImage.setOnClickListener {
            onClick(itemId)
        }
        binding.cartItemName.text = itemData.productName
        binding.cartItemVariant.text = itemData.productVariant
        binding.itemCheckbox.isChecked = itemData.isSelected
        binding.orderQtyText.text = itemData.productQuantity.toString()
        var stokString = context.getString(R.string.stock)

        if (itemData.productStock < 10) {
            binding.cartItemStock.setTextColor(context.getColor(R.color.red))
            stokString = context.getString(R.string.remaining)
            Log.d("zaky","Product stock is < 10 however its Actually ${itemData.productStock} name: ${itemData.productName}")
        } else {
            val default = binding.cartItemVariant.textColors.defaultColor
            binding.cartItemStock.setTextColor(default)
        }
        binding.cartItemStock.text = "$stokString ${itemData.productStock}"
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
            // log event
            val productBundle = bundleOf("name" to itemData.productName)
            viewModel.analytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART) {
                param(FirebaseAnalytics.Param.CURRENCY, "IDR")
                param(FirebaseAnalytics.Param.ITEMS, arrayOf(productBundle))
            }
        }
    }
}
