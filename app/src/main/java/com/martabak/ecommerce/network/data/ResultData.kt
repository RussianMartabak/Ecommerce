package com.martabak.ecommerce.network.data

data class ResultData<T>(
    val message: String,
    val success: Boolean,
    val code: Int = 200,
    val content: T? = null
)
