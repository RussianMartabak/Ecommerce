package com.martabak.ecommerce.repository

import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.network.data.product_detail.ReviewData
import javax.inject.Inject

class ProductRepository @Inject constructor(val apiService: ApiService) {
    var selectedProductID: String? = null

    suspend fun getProductReviews(): List<ReviewData> {
        try {
            val response = apiService.getProductReviews(selectedProductID!!)
            return response.data
        } catch (e: Throwable) {
            throw e
        }
    }

    suspend fun getProductDetail(): Data {
        try {
            val response = apiService.getProductDetail(selectedProductID!!)

            return response.data
        } catch (e: Throwable) {
            throw e
        }
    }
}
