package com.martabak.ecommerce.viewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.network.data.checkout.CheckoutList
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.network.data.product_detail.ProductVariant
import com.martabak.ecommerce.product_detail.ProductDetailViewModel
import com.martabak.ecommerce.repository.ProductRepository
import com.martabak.ecommerce.repository.WishlistRepository
import com.martabak.ecommerce.util.Extensions.getOrAwaitValue
import kotlinx.coroutines.flow.first
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
class ProductDetailViewModelTest {
    private lateinit var productModel: ProductDetailViewModel
    private lateinit var productData: Data
    private lateinit var productRepo: ProductRepository
    private lateinit var wishRepo: WishlistRepository

    @JvmField
    @Rule
    val jaRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        wishRepo = mock()
        productRepo = mock()
        productData = Data(
            productId = "1",
            productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
            productPrice = 1000,
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
                ProductVariant("RAM 32GB", 1000)
            )
        )
        productModel = ProductDetailViewModel(
            productRepository = productRepo,
            analytics = mock(),
            cartRepository = mock(),
            globalState = mock(),
            wishlistRepository = wishRepo
        )
        productModel.setProductData(productData)
    }

    @Test
    fun updateProductPriceTest() {
        productModel.updateProductPrice(1000)
        assertEquals(2000, productModel.productData.getOrAwaitValue().productPrice)
    }

    @Test
    fun getProductDataSuccessTest() = runTest {
        whenever(productRepo.getProductDetail()).thenReturn(productData)
        productModel.getProductData()
        assertEquals(productData, productModel.productData.getOrAwaitValue())
    }

    @Test
    fun getProductDataErrorTest() = runTest {
        whenever(productRepo.getProductDetail()).thenThrow(RuntimeException("test error"))
        productModel.getProductData()
        assertEquals(false, productModel.connectionSuccess.getOrAwaitValue())
    }

    @Test
    fun processWishlistTrueTest() = runTest {
        // set the wishlist bool by getdata and mocking the wishrepo
        whenever(productRepo.getProductDetail()).thenReturn(productData)
        whenever(wishRepo.itemExistOnWishlist("1")).thenReturn(true)
        productModel.getProductData()
        productModel.processWishlist()
        assertEquals("Item removed from wishlist", productModel.eventFlow.first())
    }

    @Test
    fun processWishlistFalseTest() = runTest {
        // set the wishlist bool by getdata and mocking the wishrepo
        whenever(productRepo.getProductDetail()).thenReturn(productData)
        whenever(wishRepo.itemExistOnWishlist("1")).thenReturn(false)
        productModel.getProductData()
        productModel.processWishlist()
        assertEquals("Item added to wishlist", productModel.eventFlow.first())
    }

    @Test
    fun parcelizeProductTest() {
        val expected = listOf(
            CheckoutData(
                productImage = productData.image[0],
                item_id = productData.productId,
                productName = productData.productName,
                productPrice = productData.productPrice,
                productQuantity = 1,
                productStock = productData.stock,
                productVariant = "RAM 16GB"
            )
        )
        val expectedList = CheckoutList(expected)
        assertEquals(expectedList, productModel.parcelizeProduct())
    }
}
