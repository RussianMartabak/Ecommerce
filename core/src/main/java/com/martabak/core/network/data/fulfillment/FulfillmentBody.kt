package com.martabak.core.network.data.fulfillment

data class FulfillmentBody(
    val payment: String,
    val items: List<FulfillmentItem>
)

data class FulfillmentItem(
    val productId: String,
    val variantName: String,
    val quantity: Int
)
