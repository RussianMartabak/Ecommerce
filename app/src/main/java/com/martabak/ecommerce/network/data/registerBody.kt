package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class registerBody(
    val email: String,
    val firebaseToken: String,
    val password: String
)