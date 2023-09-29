package com.martabak.ecommerce.main.transaction.adapters

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.TransactionItemBinding
import com.martabak.ecommerce.network.data.transaction.TransactionData
import java.text.NumberFormat

class TransactionViewHolder(
    private val binding: TransactionItemBinding,
    private val onClick: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TransactionData) {
        binding.transactionImage.load(item.image) {
            error(R.drawable.thumbnail)
        }
        binding.transactionName.text = item.name
        binding.transactionDate.text = item.date
        val formattedPrice = NumberFormat.getInstance().format(item.total).replace(",", ".")
        binding.transactionTotal.text = "Rp$formattedPrice"
        binding.transactionItemCount.text = "${item.items.size} barang"
        if (item.review != null) binding.reviewButton.isVisible = false
        binding.reviewButton.setOnClickListener {
            onClick(item.invoiceId)
        }
    }
}
