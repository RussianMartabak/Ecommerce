package com.martabak.ecommerce.networkTest


import com.martabak.core.network.data.fulfillment.FulfillmentBody
import com.martabak.core.network.data.fulfillment.FulfillmentData
import com.martabak.core.network.data.fulfillment.FulfillmentResponse
import com.martabak.core.network.data.rating.RatingBody
import com.martabak.core.network.data.rating.RatingResponse
import com.martabak.core.network.data.transaction.TransactionData
import com.martabak.core.network.data.transaction.TransactionItem
import com.martabak.core.network.data.transaction.TransactionResponse
import com.martabak.ecommerce.networkTest.util.BasedNetworkTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FulfillmentTest : BasedNetworkTest() {

    @Test
    fun transactionApiTest() {
        mockwebServer.enqueueResponse("fulfillment/TransactionMockResponse.json")

        val expected = TransactionResponse(
            code = 200,
            message = "OK",
            data = listOf(
                TransactionData(
                    invoiceId = "1",
                    date = "1",
                    image = "1.jpg",
                    items = listOf(
                        TransactionItem(productId = "1", variantName = "1", quantity = 2)
                    ),
                    name = "ASUS ROG",
                    payment = "bca",
                    total = 48998000,
                    rating = 4,
                    review = "LGTM",
                    status = true,
                    time = "1"
                )
            )
        )

        runBlocking {
            val actual = apiService.getTransactions()
            assertEquals(actual, expected)
        }
    }

    @Test
    fun ratingApiTest() {
        mockwebServer.enqueueResponse("fulfillment/RatingMockResponse.json")

        val expected = RatingResponse(200, "success")
        val ratingBody = RatingBody("aa", 9, "bad")

        runBlocking {
            val actual = apiService.postRating(ratingBody)
            assertEquals(actual, expected)
        }
    }

    @Test
    fun fulfillmentApiTest() {
        mockwebServer.enqueueResponse("fulfillment/FullfillmentMockResponse.json")

        val expected = FulfillmentResponse(
            code = 200,
            message = "OK",
            data = FulfillmentData(
                invoiceId = "ba47402c-d263-49d3-a1f8-759ae59fa4a1",
                status = true,
                date = "09 Jun 2023",
                time = "08:53",
                payment = "Bank BCA",
                total = 48998000
            )
        )
        val fufillmentBody = FulfillmentBody(payment = "bca", items = listOf())

        runBlocking {
            val actual = apiService.sendForFulfillment(fufillmentBody)
            assertEquals(actual, expected)
        }
    }
}
