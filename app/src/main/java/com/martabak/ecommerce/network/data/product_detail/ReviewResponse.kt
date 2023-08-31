package com.martabak.ecommerce.network.data.product_detail

data class ReviewData(
    val userName: String,
    val userImage: String,
    val userRating: Int,
    val userReview: String
)
data class ReviewResponse(
    val code: Int,
    val message: String,
    val `data`: List<ReviewData>
)