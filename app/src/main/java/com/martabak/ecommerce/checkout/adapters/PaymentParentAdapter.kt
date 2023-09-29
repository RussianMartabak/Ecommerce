package com.martabak.ecommerce.checkout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.martabak.ecommerce.databinding.PaymentItemParentBinding
import com.martabak.ecommerce.network.data.payment.PaymentData

class PaymentParentAdapter(var onClick: (String, String) -> Unit) : ListAdapter<PaymentData, PaymentParentViewHolder>(
    PaymentParentComparator
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentParentViewHolder {
        val binding =
            PaymentItemParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentParentViewHolder(binding, parent.context, onClick)
    }

    override fun onBindViewHolder(holder: PaymentParentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object PaymentParentComparator : DiffUtil.ItemCallback<PaymentData>() {
    override fun areItemsTheSame(oldItem: PaymentData, newItem: PaymentData): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: PaymentData, newItem: PaymentData): Boolean {
        return oldItem == newItem
    }
}
