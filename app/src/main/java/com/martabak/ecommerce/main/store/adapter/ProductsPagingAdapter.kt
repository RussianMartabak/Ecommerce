package com.martabak.ecommerce.main.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.martabak.core.network.data.Product
import com.martabak.ecommerce.databinding.ProductGridItemBinding
import com.martabak.ecommerce.databinding.ProductLinearItemBinding

class ProductsPagingAdapter(val onClick: (String) -> Unit) :
    PagingDataAdapter<Product, RecyclerView.ViewHolder>(ProductDiffCallback) {

    private var gridMode = false
    fun setGridMode(b: Boolean) {
        gridMode = b
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is LinearViewHolder) {
            holder.bind(item!!)
        } else {
            (holder as GridViewHolder).bind(item!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (gridMode) {
            val binding =
                ProductGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GridViewHolder(binding, onClick, parent.context)
        } else {
            val binding =
                ProductLinearItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LinearViewHolder(binding, onClick, parent.context)
        }
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
