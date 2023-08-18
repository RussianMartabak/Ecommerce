package com.martabak.ecommerce.network

import com.martabak.ecommerce.network.data.loginBody
import com.martabak.ecommerce.network.data.loginResponse
import com.martabak.ecommerce.network.data.registerBody
import com.martabak.ecommerce.network.data.registerResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface backendApiService {
    @Headers("API_KEY: 6f8856ed-9189-488f-9011-0ff4b6c08edc")
    @POST("register")
    suspend fun postRegister(@Body registerBody: registerBody) : registerResponse

    @Headers("API_KEY: 6f8856ed-9189-488f-9011-0ff4b6c08edc")
    @POST("login")
    suspend fun postLogin(@Body loginBody: loginBody) : loginResponse
}