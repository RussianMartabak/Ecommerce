package com.martabak.ecommerce.network.data.prelogin

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class profileResponse(
    val code: Int,
    val message: String,
    val `data`: dataProfile
)
