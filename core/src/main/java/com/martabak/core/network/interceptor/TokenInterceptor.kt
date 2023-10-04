package com.martabak.core.network.interceptor

import android.content.SharedPreferences
import com.martabak.core.util.GlobalUtils.getBearerToken
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor (
    val userPref: SharedPreferences,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .addHeader("Authorization", userPref.getBearerToken())
            .build()

        return chain.proceed(request)
    }
}
