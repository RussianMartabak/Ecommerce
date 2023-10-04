package com.martabak.ecommerce.main.store.adapter

import android.content.Context
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.core.network.data.Product
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.ProductLinearItemBinding
import java.text.NumberFormat

class LinearViewHolder(
    private var binding: ProductLinearItemBinding,
    val onClick: (String) -> Unit,
    val context: Context
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        binding.productTitle.text = product.productName
        val formattedPrice =
            NumberFormat.getInstance().format(product.productPrice).replace(",", ".")
        binding.productPrice.text = "Rp$formattedPrice"
        binding.productSeller.text = product.store
        val formattedRating = String.format("%.1f", product.productRating)
        val soldString = context.getString(R.string.sold)
        binding.productInfo.text = "$formattedRating | $soldString ${product.sale}"
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
