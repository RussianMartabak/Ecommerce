package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class dataProfile(
    val userName: String,
    val userImage: String
)