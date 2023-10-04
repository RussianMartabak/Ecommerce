package com.martabak.ecommerce.networkTest.util

import com.martabak.core.network.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.nio.charset.StandardCharsets

abstract class BasedNetworkTest {
    protected val mockwebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    protected val apiService: ApiService = Retrofit.Builder()
        .baseUrl(mockwebServer.url("/"))
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(ApiService::class.java)

    fun MockWebServer.enqueueResponse(filepath: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(filepath)
        val source = inputStream!!.source().buffer()
        source!!.let {
            this.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(it.readString(StandardCharsets.UTF_8))
            )
        }
    }
}
