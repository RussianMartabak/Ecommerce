package com.martabak.ecommerce.network.data.rating

data class RatingBody(
    val invoiceId: String,
    val rating: Int,
    val review: String
)