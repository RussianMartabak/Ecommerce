package com.martabak.ecommerce.viewModelTest

import com.martabak.core.network.ApiService
import com.martabak.core.network.data.transaction.TransactionData
import com.martabak.core.network.data.transaction.TransactionItem
import com.martabak.core.network.data.transaction.TransactionResponse
import com.martabak.ecommerce.main.transaction.TransactionViewModel
import com.martabak.ecommerce.status.StatusParcel
import com.martabak.ecommerce.util.Extensions.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TransactionViewModelTest {
    private lateinit var transModel: TransactionViewModel
    private lateinit var mockApi: ApiService

    @Before
    fun setup() {
        mockApi = mock()
        transModel = TransactionViewModel(mockApi)
    }

    @Test
    fun getTransactionListAndParcelize() = runTest {
        val response = TransactionResponse(
            200,
            "OK",
            listOf(
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
        whenever(mockApi.getTransactions()).thenReturn(response)
        val expected = response.data
        transModel.getTransactions()
        assertEquals(expected, transModel.transactionList.getOrAwaitValue())
        val expected2 = StatusParcel(
            invoiceId = "1",
            date = "1",
            invoiceSum = 48998000,
            time = "1",
            payment = "bca"
        )
        assertEquals(expected2, transModel.getStatusParcel("1"))
    }
}
