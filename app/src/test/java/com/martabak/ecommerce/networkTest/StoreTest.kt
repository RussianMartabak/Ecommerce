package com.martabak.ecommerce.networkTest

import com.martabak.ecommerce.network.data.Data
import com.martabak.ecommerce.network.data.Product
import com.martabak.ecommerce.network.data.ProductsResponse
import com.martabak.ecommerce.network.data.SearchResponse
import com.martabak.ecommerce.networkTest.util.BasedNetworkTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class StoreTest : BasedNetworkTest() {
    @Test
    fun productsApiTest() {
        mockwebServer.enqueueResponse("store/ProductsMockResponse.json")

        val expected = ProductsResponse(
            code = 200, message = "OK", data = Data(
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

        runBlocking {
            val actual = apiService.postProducts("asus", null, null, null, null, null, null)
            assertEquals(actual, expected)
        }
    }

    @Test
    fun searchApiTest() {
        mockwebServer.enqueueResponse("store/SearchMockResponse.json")

        val expected = SearchResponse(code = 200, message = "OK", data = listOf(
            "Lenovo Legion 3",
            "Lenovo Legion 5",
            "Lenovo Legion 7",
            "Lenovo Ideapad 3",
            "Lenovo Ideapad 5",
            "Lenovo Ideapad 7"
            )
        )

        runBlocking {
            val actual = apiService.postSearch("lenovo")
            assertEquals(actual, expected)
        }
    }

}