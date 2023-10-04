package com.martabak.core.network

import com.martabak.core.network.data.ProductsResponse
import com.martabak.core.network.data.SearchResponse
import com.martabak.core.network.data.fulfillment.FulfillmentBody
import com.martabak.core.network.data.fulfillment.FulfillmentResponse
import com.martabak.core.network.data.payment.PaymentResponse
import com.martabak.core.network.data.prelogin.LoginBody
import com.martabak.core.network.data.prelogin.RefreshBody
import com.martabak.core.network.data.prelogin.RefreshResponse
import com.martabak.core.network.data.prelogin.RegisterBody
import com.martabak.core.network.data.prelogin.RegisterResponse
import com.martabak.core.network.data.prelogin.loginResponse
import com.martabak.core.network.data.prelogin.profileResponse
import com.martabak.core.network.data.product_detail.ProductDetailResponse
import com.martabak.core.network.data.product_detail.ReviewResponse
import com.martabak.core.network.data.rating.RatingBody
import com.martabak.core.network.data.rating.RatingResponse
import com.martabak.core.network.data.transaction.TransactionResponse
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
    suspend fun postRegister(@Body registerBody: RegisterBody): RegisterResponse

    @Headers("API_KEY: 6f8856ed-9189-488f-9011-0ff4b6c08edc")
    @POST("login")
    suspend fun postLogin(@Body loginBody: LoginBody): loginResponse

    @Headers("API_KEY: 6f8856ed-9189-488f-9011-0ff4b6c08edc")
    @POST("refresh")
    suspend fun postRefresh(@Body refreshBody: RefreshBody): RefreshResponse

    @POST("profile")
    @Multipart
    suspend fun createProfile(
        @Part userName: MultipartBody.Part,
        @Part userImage: MultipartBody.Part?
    ): profileResponse

    @POST("search")
    suspend fun postSearch(@Query("query") query: String): SearchResponse

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") productID: String): ProductDetailResponse

    @GET("review/{id}")
    suspend fun getProductReviews(@Path("id") productID: String): ReviewResponse

    @POST("products")
    suspend fun postProducts(
        @Query("search") search: String,
        @Query("brand") brand: String?,
        @Query("lowest") lowest: Int?,
        @Query("highest") highest: Int?,
        @Query("sort") sort: String?,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?
    ): ProductsResponse

    @GET("payment")
    suspend fun getPaymentMethods(): PaymentResponse

    @POST("fulfillment")
    suspend fun sendForFulfillment(@Body fulfillBody: FulfillmentBody): FulfillmentResponse

    @POST("rating")
    suspend fun postRating(@Body ratingBody: RatingBody): RatingResponse

    @GET("transaction")
    suspend fun getTransactions(): TransactionResponse
}
