package com.martabak.ecommerce.product_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.ProductDetailItemBinding

class ProductDetailAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ProductDetailAdapter.ProductDetailViewHolder>() {

    class ProductDetailViewHolder(private val binding: ProductDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            val imgUri = url.toUri().buildUpon().scheme("http").build()
            binding.productDetailImage.load(imgUri) {
                placeholder(R.drawable.thumbnail)
                error(R.drawable.thumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailViewHolder {
        val view = ProductDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ProductDetailViewHolder, position: Int) {
        val item = images[position]
        holder.bind(item)

    }
}