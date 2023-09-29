package com.martabak.ecommerce.checkout.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.martabak.ecommerce.checkout.CheckoutViewModel
import com.martabak.ecommerce.databinding.CheckoutItemBinding
import com.martabak.ecommerce.network.data.checkout.CheckoutData

class CheckoutAdapter(private val viewModel: CheckoutViewModel) : ListAdapter<CheckoutData, CheckoutViewHolder>(
    CheckoutComparator
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val binding =
            CheckoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutViewHolder(binding, viewModel, parent.context)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object CheckoutComparator : DiffUtil.ItemCallback<CheckoutData>() {
    override fun areItemsTheSame(oldItem: CheckoutData, newItem: CheckoutData): Boolean {
        return oldItem.productName == newItem.productName
    }

    override fun areContentsTheSame(oldItem: CheckoutData, newItem: CheckoutData): Boolean {
        Log.d("zaky", "Adapter arecontents the same: Old: $oldItem New: $newItem")
        return oldItem.productQuantity == newItem.productQuantity
    }
}
