package com.martabak.ecommerce.repositoryTest


import com.martabak.core.network.ApiService
import com.martabak.core.network.data.product_detail.Data
import com.martabak.core.network.data.product_detail.ProductDetailResponse
import com.martabak.core.network.data.product_detail.ProductVariant
import com.martabak.core.network.data.product_detail.ReviewData
import com.martabak.core.network.data.product_detail.ReviewResponse
import com.martabak.ecommerce.repository.ProductRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ProductRepoTest {
    private lateinit var productRepo: ProductRepository
    private lateinit var mockApi: ApiService

    @Before
    fun setup() {
        mockApi = mock()
        productRepo = ProductRepository(mockApi)
        productRepo.selectedProductID = "lol"
    }

    @Test
    fun getProductReviewsTest() = runTest {
        val response = ReviewResponse(
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
        whenever(mockApi.getProductReviews("lol")).thenReturn(response)
        assertEquals(response.data, productRepo.getProductReviews())
    }

    @Test
    fun getProductDetailTest() = runTest {
        val response = ProductDetailResponse(
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
        whenever(mockApi.getProductDetail("lol")).thenReturn(response)
        assertEquals(response.data, productRepo.getProductDetail())
    }
}
