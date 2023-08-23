package com.martabak.ecommerce.di

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.interceptor.TokenInterceptor
import com.martabak.ecommerce.repository.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    //what matters is the return type, everything else doesn't matter
    @Singleton
    @Provides
    fun provideApiService(moshi: Moshi, client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.101:8000/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        @ApplicationContext context: Context,
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(ChuckerInterceptor(context))
            addInterceptor(tokenInterceptor)
        }.build()
        return client
    }

    @Singleton
    @Provides
    fun provideSP(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("localPrefData", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideUserRepository(userPref : SharedPreferences, api : ApiService) : UserRepository {
        return UserRepository(userPref, api)
    }
}