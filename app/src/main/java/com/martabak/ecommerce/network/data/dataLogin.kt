package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class dataLogin(
    val accessToken: String,
    val expiresAt: Int,
    val refreshToken: String,
    val userImage: String,
    val userName: String
)