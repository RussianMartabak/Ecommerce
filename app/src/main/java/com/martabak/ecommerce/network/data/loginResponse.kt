package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class loginResponse(
    val code: Int,
    val `data`: dataLogin,
    val message: String
)