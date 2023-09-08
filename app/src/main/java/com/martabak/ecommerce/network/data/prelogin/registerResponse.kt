package com.martabak.ecommerce.network.data.prelogin

import com.martabak.ecommerce.network.data.prelogin.DataRegister
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    val code: Int,
    val message: String,
    val `data`: DataRegister
)