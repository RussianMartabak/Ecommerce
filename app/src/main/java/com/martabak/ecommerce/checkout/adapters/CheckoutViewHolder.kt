package com.martabak.ecommerce.checkout.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.checkout.CheckoutViewModel
import com.martabak.ecommerce.databinding.CheckoutItemBinding
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import java.text.NumberFormat

class CheckoutViewHolder(private val binding: CheckoutItemBinding, private val viewModel: CheckoutViewModel, private val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    private var id = ""
    fun bind(product: CheckoutData) {
        binding.apply {
            orderProductImage.load(product.productImage) {
                error(R.drawable.thumbnail)
            }
            orderProductName.text = product.productName
            var stock: String = context.getString(R.string.stock)

            if (product.productStock < 10) {
                orderProductStock.setTextColor(context.getColor(R.color.red))
                stock = context.getString(R.string.remaining)

            } else {
                val default = binding.orderProductVariant.textColors.defaultColor
                binding.orderProductStock.setTextColor(default)
            }
            orderProductStock.text = "$stock ${product.productStock}"
            val formattedPrice =
                NumberFormat.getInstance().format(product.productPrice).replace(",", ".")
            orderProductPrice.text = "Rp$formattedPrice"
            orderProductVariant.text = product.productVariant
            orderQtyText.text = product.productQuantity.toString()
        }
        binding.plusButton.setOnClickListener {
            viewModel.addItemCount(product.item_id)
        }
        binding.minusButton.setOnClickListener {
            viewModel.decreaseItemCount(product.item_id)
        }
    }
}
