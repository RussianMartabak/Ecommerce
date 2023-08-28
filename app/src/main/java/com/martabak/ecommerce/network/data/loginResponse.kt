package com.martabak.ecommerce.network.data

import com.martabak.ecommerce.network.data.prelogin.dataLogin
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class loginResponse(
    val code: Int,
    val message: String,
    val `data`: dataLogin
)