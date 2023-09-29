package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsResponse(
    val code: Int,
    val message: String,
    val `data`: Data

)

data class Data(
    val itemsPerPage: Int,
    val currentItemCount: Int,
    val pageIndex: Int,
    val totalPages: Int,
    val items: List<Product>
)
