package com.martabak.ecommerce.di

import com.martabak.ecommerce.network.backendApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object mainModule {

    @Singleton
    @Provides
    fun provideApiService(moshi : Moshi) : backendApiService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.100:8000/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(backendApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


}