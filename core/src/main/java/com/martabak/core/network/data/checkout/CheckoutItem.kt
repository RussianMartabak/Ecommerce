package com.martabak.core.network.data.checkout

data class CheckoutItem(
    val productId: String,
    val variantName: String,
    val quantity: Int
)
