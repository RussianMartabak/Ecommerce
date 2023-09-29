package com.martabak.ecommerce.network.interceptor

import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.martabak.ecommerce.GlobalState
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.prelogin.RefreshBody
import com.martabak.ecommerce.network.data.prelogin.RefreshResponse
import com.martabak.ecommerce.utils.GlobalUtils
import com.martabak.ecommerce.utils.GlobalUtils.getRefreshToken
import com.martabak.ecommerce.utils.GlobalUtils.putAccessToken
import com.martabak.ecommerce.utils.GlobalUtils.setRefreshToken
import com.squareup.moshi.Moshi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.runBlocking
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
    private val chucker: ChuckerInterceptor,
    private val globalState: GlobalState
) : Authenticator {

    @OptIn(InternalCoroutinesApi::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            return runBlocking {
                var currentRefreshToken = userPref.getRefreshToken()
                // first build a okhttp client
                val client = OkHttpClient.Builder().apply {
                    addInterceptor(tokenInterceptor)
                    addInterceptor(chucker)
                }.build()
                // build own retrofit
                val retrofit = Retrofit.Builder()
                    .baseUrl(GlobalUtils.BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
                    .create(ApiService::class.java)
                // send refresh request and get the new accesstoken and refreshtoken into SP

                try {
                    var refreshResponse = refresh(retrofit, currentRefreshToken)
                    userPref.putAccessToken(refreshResponse.data.accessToken)
                    userPref.setRefreshToken(refreshResponse.data.refreshToken)
                    // after all set re send the old request with new bearer token
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer ${refreshResponse.data.accessToken}")
                        .build()
                } catch (e: Throwable) {
                    globalState.sendLogoutEvent()
                    return@runBlocking null
                }
            }
        }
    }

    private suspend fun refresh(retrofit: ApiService, currentRefreshToken: String): RefreshResponse {
        return retrofit.postRefresh(RefreshBody(currentRefreshToken))
    }
}
