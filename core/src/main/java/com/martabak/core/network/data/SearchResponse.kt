package com.martabak.core.network.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    val code: Int,
    val message: String,
    val `data`: List<String>
)
