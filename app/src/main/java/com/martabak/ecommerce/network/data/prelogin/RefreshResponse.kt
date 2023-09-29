package com.martabak.ecommerce.network.data.prelogin

data class RefreshResponse(
    val code: Int,
    val message: String,
    val `data`: RefreshData
)

data class RefreshData(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Int
)
