package com.martabak.ecommerce.main.store.adapter

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.ProductLinearItemBinding
import com.martabak.ecommerce.network.data.Product
import java.text.NumberFormat

class LinearViewHolder(private var binding: ProductLinearItemBinding, val onClick : (String) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        binding.productTitle.text = product.productName
        val formattedPrice =
            NumberFormat.getInstance().format(product.productPrice).replace(",", ".")
        binding.productPrice.text = "Rp$formattedPrice"
        binding.productSeller.text = product.store
        val formattedRating = String.format("%.1f", product.productRating)
        binding.productInfo.text = "$formattedRating | Terjual ${product.sale}"
        val imgUri = product.image.toUri().buildUpon().scheme("http").build()
        binding.productImage.load(imgUri) {
            placeholder(R.drawable.thumbnail)
            error(R.drawable.thumbnail)
        }

        binding.rootLinearCard.setOnClickListener {
            onClick(product.productId)
        }
    }
}