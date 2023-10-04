package com.martabak.core.network.data.payment

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentResponse(
    val code: Int,
    val message: String,
    val `data`: List<PaymentData>
)

@JsonClass(generateAdapter = true)
data class PaymentData(
    val title: String,
    val item: List<PaymentItem>
)

@JsonClass(generateAdapter = true)
data class PaymentItem(
    val label: String,
    val image: String,
    val status: Boolean
)
