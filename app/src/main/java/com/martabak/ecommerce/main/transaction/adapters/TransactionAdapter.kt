package com.martabak.ecommerce.main.transaction.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.martabak.core.network.data.transaction.TransactionData
import com.martabak.ecommerce.databinding.TransactionItemBinding


class TransactionAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<TransactionData, TransactionViewHolder>(TransactionComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object TransactionComparator : DiffUtil.ItemCallback<TransactionData>() {
    override fun areItemsTheSame(oldItem: TransactionData, newItem: TransactionData): Boolean {
        return oldItem.invoiceId == newItem.invoiceId
    }

    override fun areContentsTheSame(oldItem: TransactionData, newItem: TransactionData): Boolean {
        return oldItem == newItem
    }
}
