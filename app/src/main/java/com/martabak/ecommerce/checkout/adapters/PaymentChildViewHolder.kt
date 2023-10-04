package com.martabak.ecommerce.checkout.adapters

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.core.network.data.payment.PaymentItem
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.PaymentItemChildBinding

class PaymentChildViewHolder(
    private val binding: PaymentItemChildBinding,
    private val onClick: (String, String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(childData: PaymentItem) {
        binding.methodImage.load(childData.image) {
            error(R.drawable.thumbnail)
        }
        binding.methodName.text = childData.label
        if (!childData.status) {
            binding.paymentChildRoot.alpha = 0.5F
        } else {
            binding.paymentChildRoot.setOnClickListener {
                onClick(childData.label, childData.image)
            }
        }
    }
}
