package com.martabak.ecommerce.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.martabak.ecommerce.GlobalState
import com.martabak.ecommerce.database.AppDatabase
import com.martabak.ecommerce.database.CartDao
import com.martabak.ecommerce.database.NotifDao
import com.martabak.ecommerce.database.WishlistDao
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.interceptor.TokenAuthenticator
import com.martabak.ecommerce.network.interceptor.TokenInterceptor
import com.martabak.ecommerce.repository.ProductRepository
import com.martabak.ecommerce.repository.StoreRepository
import com.martabak.ecommerce.repository.UserRepository
import com.martabak.ecommerce.utils.GlobalUtils
import com.martabak.ecommerce.utils.GlobalUtils.DATABASE_NAME
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

    @Singleton
    @Provides
    fun provideChucker(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        tokenInterceptor: TokenInterceptor,
        chucker: ChuckerInterceptor,
        authenticator: TokenAuthenticator
    ): OkHttpClient {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(chucker)
            addInterceptor(tokenInterceptor)
            authenticator(authenticator)
        }.build()
        return client
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    //what matters is the return type, everything else doesn't matter
    @Singleton
    @Provides
    fun provideApiService(moshi: Moshi, client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(GlobalUtils.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideSP(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("localPrefData", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideUserRepository(userPref: SharedPreferences, api: ApiService): UserRepository {
        return UserRepository(userPref, api)
    }

    @Singleton
    @Provides
    fun provideStoreRepository(apiService: ApiService): StoreRepository {
        return StoreRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideProductRepository(apiService: ApiService): ProductRepository {
        return ProductRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideGlobalStateClass(): GlobalState {
        return GlobalState()
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
        return db
    }

    @Singleton
    @Provides
    fun provideCartDao(db : AppDatabase) : CartDao {
        return db.cartDao()
    }

    @Singleton
    @Provides
    fun provideWishlistDao(db : AppDatabase) : WishlistDao {
        return db.wishlistDao()
    }

    @Singleton
    @Provides
    fun provideNotifDao(db : AppDatabase) : NotifDao {
        return db.notifDao()
    }

    @Singleton
    @Provides
    fun provideRemoteConfig() : FirebaseRemoteConfig {
        val remoteConfig : FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 100
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        return remoteConfig
    }



}