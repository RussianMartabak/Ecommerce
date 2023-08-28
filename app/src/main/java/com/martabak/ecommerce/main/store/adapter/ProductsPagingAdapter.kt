package com.martabak.ecommerce.main.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.ProductLinearItemBinding
import com.martabak.ecommerce.network.data.Product
import java.text.NumberFormat

class ProductsPagingAdapter() :
    PagingDataAdapter<Product, ProductsPagingAdapter.ProductViewHolder>(ProductDiffCallback) {

    class ProductViewHolder(private var binding: ProductLinearItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productTitle.text = product.productName
            val formattedPrice = NumberFormat.getInstance().format(product.productPrice).replace(",", ".")
            binding.productPrice.text = "Rp$formattedPrice"
            binding.productSeller.text = product.store
            val formattedRating = String.format("%.1f", product.productRating)
            binding.productInfo.text = "$formattedRating | Terjual ${product.sale}"
            val imgUri = product.image.toUri().buildUpon().scheme("http").build()
            binding.productImage.load(imgUri) {
                placeholder(R.drawable.thumbnail)
                error(R.drawable.thumbnail)
            }
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductLinearItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }
}

object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productId == newItem.productId
    }

}