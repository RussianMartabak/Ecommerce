package com.martabak.core.network.data.prelogin

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginBody(
    val email: String,
    val password: String,
    val firebaseToken: String
)
