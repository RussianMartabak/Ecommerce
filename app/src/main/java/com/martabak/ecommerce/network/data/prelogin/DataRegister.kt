package com.martabak.ecommerce.network.data.prelogin

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataRegister(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Int
)