package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class dataRegister(
    val accessToken: String,
    val expiresAt: Int,
    val refreshToken: String
)