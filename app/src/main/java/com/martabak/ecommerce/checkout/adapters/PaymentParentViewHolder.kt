package com.martabak.ecommerce.checkout.adapters

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martabak.core.network.data.payment.PaymentData
import com.martabak.ecommerce.databinding.PaymentItemParentBinding

class PaymentParentViewHolder(
    private val binding: PaymentItemParentBinding,
    private val context: Context,
    private val onClick: (String, String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(parentData: PaymentData) {
        binding.paymentTitle.text = parentData.title
        val adapter = PaymentChildAdapter(onClick)
        binding.paymentParentRecycler.adapter = adapter
        binding.paymentParentRecycler.layoutManager = LinearLayoutManager(context)
        adapter.submitList(parentData.item)
    }
}
