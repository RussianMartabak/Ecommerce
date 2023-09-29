package com.martabak.ecommerce.viewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.martabak.ecommerce.network.data.product_detail.ReviewData
import com.martabak.ecommerce.product_detail.ReviewViewModel
import com.martabak.ecommerce.repository.ProductRepository
import com.martabak.ecommerce.util.Extensions.getOrAwaitValue
import com.martabak.ecommerce.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ReviewViewModelTest {
    private lateinit var reviewModel: ReviewViewModel
    private lateinit var productRepo: ProductRepository
    private lateinit var reviewData: List<ReviewData>

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @JvmField
    @Rule
    val jaRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        productRepo = mock()
        reviewModel = ReviewViewModel(productRepo)
        reviewData = listOf(
            ReviewData(
                userName = "John",
                userImage = "a",
                userReview = "aa",
                userRating = 4
            ),
            ReviewData(userName = "Doe", userImage = "a", userReview = "aa", userRating = 5)
        )
    }

    @Test
    fun getReviewsTest() = runTest {
        whenever(productRepo.getProductReviews()).thenReturn(reviewData)
        reviewModel.getReviews()
        assertEquals(reviewData, reviewModel.reviewData.getOrAwaitValue())
    }
}
