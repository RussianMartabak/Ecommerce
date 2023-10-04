package com.martabak.ecommerce.main.store.adapter

import android.content.Context
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.core.network.data.Product
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.ProductGridItemBinding
import java.text.NumberFormat

class GridViewHolder(private var binding: ProductGridItemBinding, val onClick: (String) -> Unit, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        binding.productTitleGrid.text = product.productName
        val formattedPrice =
            NumberFormat.getInstance().format(product.productPrice).replace(",", ".")
        binding.productPriceGrid.text = "Rp$formattedPrice"
        binding.productSellerGrid.text = product.store
        val formattedRating = String.format("%.1f", product.productRating)
        val soldString = context.resources.getString(R.string.sold)
        binding.productInfoGrid.text = "$formattedRating | $soldString ${product.sale}"

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
