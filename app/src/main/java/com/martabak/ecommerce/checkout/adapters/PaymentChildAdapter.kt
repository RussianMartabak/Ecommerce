package com.martabak.ecommerce.checkout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.martabak.ecommerce.databinding.PaymentItemChildBinding
import com.martabak.ecommerce.network.data.payment.PaymentItem

class PaymentChildAdapter(private val onClick: (String, String) -> Unit) : ListAdapter<PaymentItem, PaymentChildViewHolder>(
    PaymentChildComparator
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentChildViewHolder {
        val binding =
            PaymentItemChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentChildViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: PaymentChildViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object PaymentChildComparator : DiffUtil.ItemCallback<PaymentItem>() {
    override fun areItemsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
        return oldItem.label == newItem.label
    }

    override fun areContentsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
        return oldItem == newItem
    }
}
