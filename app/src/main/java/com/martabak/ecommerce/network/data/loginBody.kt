package com.martabak.ecommerce.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class loginBody(
    val email: String,
    val password: String,
    val firebaseToken: String
)