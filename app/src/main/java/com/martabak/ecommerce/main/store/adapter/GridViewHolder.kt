package com.martabak.ecommerce.main.store.adapter

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.ProductGridItemBinding
import com.martabak.ecommerce.network.data.Product
import java.text.NumberFormat

class GridViewHolder(private var binding: ProductGridItemBinding, val onClick : (String) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(product: Product) {
        binding.productTitleGrid.text = product.productName
        val formattedPrice =
            NumberFormat.getInstance().format(product.productPrice).replace(",", ".")
        binding.productPriceGrid.text = "Rp$formattedPrice"
        binding.productSellerGrid.text = product.store
        val formattedRating = String.format("%.1f", product.productRating)
        binding.productInfoGrid.text = "$formattedRating | Terjual ${product.sale}"


        val imgUri = product.image.toUri().buildUpon().scheme("http").build()
        binding.productImageGrid.load(imgUri) {
            placeholder(R.drawable.thumbnail)
            error(R.drawable.thumbnail)
        }
        binding.rootGridCard.setOnClickListener {
            onClick(product.productId)
        }
    }
}