package com.martabak.ecommerce.network.data.payment

data class PaymentResponse(
    val code: Int,
    val message: String,
    val `data`: List<PaymentData>
)

data class PaymentData(
    val title: String,
    val item: List<PaymentItem>
)

data class PaymentItem(
    val label: String,
    val image: String,
    val status: Boolean
)