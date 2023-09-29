package com.martabak.ecommerce.repositoryTest

import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.Data
import com.martabak.ecommerce.network.data.Product
import com.martabak.ecommerce.network.data.ProductsResponse
import com.martabak.ecommerce.network.data.ResultData
import com.martabak.ecommerce.network.data.SearchResponse
import com.martabak.ecommerce.repository.StoreRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class StoreRepoTest {
    private lateinit var storeRepo: StoreRepository
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        apiService = mock()
        storeRepo = StoreRepository(apiService)
    }

    @Test
    fun getSearchItemsTest() = runTest {
        val data = listOf(
            "Lenovo Legion 3",
            "Lenovo Legion 5",
            "Lenovo Legion 7",
            "Lenovo Ideapad 3",
            "Lenovo Ideapad 5",
            "Lenovo Ideapad 7"
        )
        val expected = ResultData("OK", true, 200, data)
        whenever(apiService.postSearch("")).thenReturn(SearchResponse(200, "OK", data))
        assertEquals(expected, storeRepo.getSearchItems(""))
    }

    @Test
    fun getProductsTest() = runTest {
        val expected = ProductsResponse(
            code = 200,
            message = "OK",
            data = Data(
                itemsPerPage = 10,
                currentItemCount = 10,
                pageIndex = 1,
                totalPages = 3,
                items = listOf(
                    Product(
                        productId = "601bb59a-4170-4b0a-bd96-f34538922c7c",
                        productName = "Lenovo Legion 3",
                        productPrice = 10000000,
                        image = "image1",
                        brand = "Lenovo",
                        store = "LenovoStore",
                        sale = 2,
                        productRating = 4.0
                    ),
                    Product(
                        productId = "3134a179-dff6-464f-b76e-d7507b06887b",
                        productName = "Lenovo Legion 5",
                        productPrice = 15000000,
                        image = "image1",
                        brand = "Lenovo",
                        store = "LenovoStore",
                        sale = 4,
                        productRating = 4.0
                    )
                )
            )
        )
        whenever(apiService.postProducts("", null, null, null, null, 6, 1)).thenReturn(expected)
        assertEquals(expected, storeRepo.getProducts())
    }
}
