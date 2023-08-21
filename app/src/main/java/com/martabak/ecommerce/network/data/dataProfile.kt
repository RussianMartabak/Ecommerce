package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class dataProfile(
    val userImage: String,
    val userName: String
)