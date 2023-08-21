package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class profleResponse(
    val code: Int,
    val dataProfile: dataProfile,
    val message: String
)