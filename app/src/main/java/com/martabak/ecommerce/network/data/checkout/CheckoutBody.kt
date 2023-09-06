package com.martabak.ecommerce.network.data.checkout

data class CheckoutBody(
    val payment: String,
    val items: List<CheckoutItem>
)