package com.martabak.ecommerce.networkTest


import com.martabak.core.network.data.product_detail.Data
import com.martabak.core.network.data.product_detail.ProductDetailResponse
import com.martabak.core.network.data.product_detail.ProductVariant
import com.martabak.core.network.data.product_detail.ReviewData
import com.martabak.core.network.data.product_detail.ReviewResponse
import com.martabak.ecommerce.networkTest.util.BasedNetworkTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductDetailTest : BasedNetworkTest() {
    @Test
    fun productReviewApiTest() {
        mockwebServer.enqueueResponse("detail/ProductReviewMockResponse.json")

        val expected = ReviewResponse(
            code = 200,
            message = "OK",
            data = listOf(
                ReviewData(
                    userName = "John",
                    userImage = "a",
                    userReview = "aa",
                    userRating = 4
                ),
                ReviewData(userName = "Doe", userImage = "a", userReview = "aa", userRating = 5)
            )
        )

        runBlocking {
            val actual = apiService.getProductReviews("1945")
            assertEquals(actual, expected)
        }
    }

    @Test
    fun productDetailApiTest() {
        mockwebServer.enqueueResponse("detail/ProductDetailMockResponse.json")

        val expected = ProductDetailResponse(
            code = 200,
            message = "OK",
            data = Data(
                productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
                productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
                productPrice = 24499000,
                image = listOf("berlin.jpg", "tokyo.jpg", "rome.jpg"),
                brand = "Asus",
                description = "ASUS ROG Strix G17™  6800H Mobile Processor",
                "AsusStore",
                sale = 12,
                stock = 2,
                totalRating = 7,
                totalReview = 5,
                totalSatisfaction = 100,
                productRating = 5.0,
                productVariant = listOf(
                    ProductVariant("RAM 16GB", 0),
                    ProductVariant("RAM 32GB", 1000000)
                )
            )
        )

        runBlocking {
            val actual = apiService.getProductDetail("Takahashi Kageyasu")
            assertEquals(actual, expected)
        }
    }
}
