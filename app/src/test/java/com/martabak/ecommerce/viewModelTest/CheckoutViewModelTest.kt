package com.martabak.ecommerce.viewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.martabak.ecommerce.checkout.CheckoutViewModel
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentBody
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentData
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentItem
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentResponse
import com.martabak.ecommerce.util.Extensions.getOrAwaitValue
import com.martabak.ecommerce.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import okhttp3.internal.checkOffsetAndCount
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CheckoutViewModelTest {
    private lateinit var checkoutModel: CheckoutViewModel
    private lateinit var mockApi: ApiService
    private lateinit var testFulfillmentBody : FulfillmentBody

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @JvmField
    @Rule
    val jaRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        mockApi = mock()
        checkoutModel = CheckoutViewModel(mockApi, mock())
        //fill out livedata that will be required
        checkoutModel.setPayment("bca")
        checkoutModel.submitItemList(listOf(CheckoutData("1.jpg", "asus", 1000, 100, "16", 100, "10")))
        testFulfillmentBody = FulfillmentBody("bca", listOf(FulfillmentItem("10", "16", 100)))
    }

    @Test
    fun sendFulfillmentSuccessTest() = runTest {
        val response = FulfillmentResponse(200, "OK", FulfillmentData("", true, "1", "2", "a", 10))
        whenever(mockApi.sendForFulfillment(testFulfillmentBody)).thenReturn(response)
        assertEquals(Unit, checkoutModel.sendFulfillment())
    }

    @Test
    fun sendFulfillmentErrorTest() = runTest {
        whenever(mockApi.sendForFulfillment(testFulfillmentBody)).thenThrow(RuntimeException("test error"))
        checkoutModel.sendFulfillment()
        assertEquals("test error", checkoutModel.errorMessage.getOrAwaitValue())
    }

    @Test
    fun addAndSubstractItemCountTest() =  runTest {
        var expected = listOf(CheckoutData("1.jpg", "asus", 1000, 100, "16", 101, "10"))
        checkoutModel.addItemCount("10")
        assertEquals(expected, checkoutModel.itemList.getOrAwaitValue())
        expected = listOf(CheckoutData("1.jpg", "asus", 1000, 100, "16", 100, "10"))
        checkoutModel.decreaseItemCount("10")
        assertEquals(expected, checkoutModel.itemList.getOrAwaitValue())
    }




}