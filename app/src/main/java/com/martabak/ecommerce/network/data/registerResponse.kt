package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class registerResponse(
    val code: Int,
    val `data`: dataRegister,
    val message: String
)