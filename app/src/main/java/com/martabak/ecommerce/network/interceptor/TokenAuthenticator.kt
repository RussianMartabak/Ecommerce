package com.martabak.ecommerce.network.interceptor


import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.utils.GlobalUtils
import com.martabak.ecommerce.utils.GlobalUtils.getRefreshToken
import com.squareup.moshi.Moshi
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    val userPref: SharedPreferences,
    val moshi: Moshi,
    private val tokenInterceptor: TokenInterceptor,
    private val chucker : ChuckerInterceptor
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        var refreshToken = userPref.getRefreshToken()

        //first build a okhttp client
        val client = OkHttpClient.Builder().apply {
            addInterceptor(tokenInterceptor)
            addInterceptor(chucker)
            authenticator(this@TokenAuthenticator)
        }.build()
        //build own retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalUtils.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
        // send refresh request and get the new accesstoken and refreshtoken into SP

        TODO("Not yet implemented")
        // will re return original request with the correct access token
    }
}