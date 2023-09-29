package com.martabak.ecommerce.network.data.prelogin

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class dataLogin(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Int,
    val userName: String,
    val userImage: String
)
