package com.martabak.core.network.data.prelogin

import com.martabak.core.network.data.prelogin.dataProfile
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class profileResponse(
    val code: Int,
    val message: String,
    val `data`: dataProfile
)
