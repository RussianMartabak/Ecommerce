package com.martabak.ecommerce.network.data.product_detail

data class ProductDetailResponse(
    val code: Int,
    val message: String,
    val `data`: Data
)

data class Data(
    val productId: String,
    val productName: String,
    var productPrice: Int,
    val image: List<String>,
    val brand: String,
    val description: String,
    val store: String,
    val sale: Int,
    val stock: Int,
    val totalRating: Int,
    val totalReview: Int,
    val totalSatisfaction: Int,
    val productRating: Double,
    val productVariant: List<ProductVariant>
)

data class ProductVariant(
    val variantName: String,
    val variantPrice: Int
)
