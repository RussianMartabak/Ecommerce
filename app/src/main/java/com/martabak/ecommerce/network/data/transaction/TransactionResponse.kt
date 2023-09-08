package com.martabak.ecommerce.network.data.transaction

data class TransactionResponse(
    val code: Int,
    val message: String,
    val `data`: List<TransactionData>
)

data class TransactionData(
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int,
    val items: List<TransactionItem>,
    val rating: Int,
    val review: String,
    val image: String,
    val name: String
)

data class TransactionItem(
    val productId: String,
    val variantName: String,
    val quantity: Int
)