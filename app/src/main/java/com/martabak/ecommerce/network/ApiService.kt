package com.martabak.ecommerce.network

import com.martabak.ecommerce.network.data.ProductsResponse
import com.martabak.ecommerce.network.data.prelogin.LoginBody
import com.martabak.ecommerce.network.data.prelogin.loginResponse
import com.martabak.ecommerce.network.data.profileResponse
import com.martabak.ecommerce.network.data.prelogin.registerBody
import com.martabak.ecommerce.network.data.RegisterResponse
import com.martabak.ecommerce.network.data.SearchResponse
import com.martabak.ecommerce.network.data.prelogin.RefreshBody
import com.martabak.ecommerce.network.data.prelogin.RefreshResponse
import com.martabak.ecommerce.network.data.product_detail.ProductDetailResponse
import com.martabak.ecommerce.network.data.product_detail.ReviewResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers("API_KEY: 6f8856ed-9189-488f-9011-0ff4b6c08edc")
    @POST("register")
    suspend fun postRegister(@Body registerBody: registerBody): RegisterResponse

    @Headers("API_KEY: 6f8856ed-9189-488f-9011-0ff4b6c08edc")
    @POST("login")
    suspend fun postLogin(@Body loginBody: LoginBody): loginResponse

    @Headers("API_KEY: 6f8856ed-9189-488f-9011-0ff4b6c08edc")
    @POST("refresh")
    suspend fun postRefresh(@Body refreshBody : RefreshBody) : RefreshResponse

    @POST("profile")
    @Multipart
    suspend fun createProfile(
        @Part userName: MultipartBody.Part,
        @Part userImage: MultipartBody.Part?
    ): profileResponse

    @POST("search")
    suspend fun postSearch(@Query("query") query: String): SearchResponse

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") productID : String) : ProductDetailResponse

    @GET("review/{id}")
    suspend fun getProductReviews(@Path("id") productID: String) : ReviewResponse

    @POST("products")
    suspend fun postProducts(
        @Query("search") search: String,
        @Query("brand") brand: String?,
        @Query("lowest") lowest: Int?,
        @Query("highest") highest: Int?,
        @Query("sort") sort: String?,
        @Query("limit") limit : Int?,
        @Query("page") page : Int?
    ) : ProductsResponse


}