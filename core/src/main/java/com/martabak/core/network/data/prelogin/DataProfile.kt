package com.martabak.core.network.data.prelogin

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class dataProfile(
    val userName: String,
    val userImage: String
)
