package com.martabak.ecommerce.viewModelTest

import com.martabak.ecommerce.main.store.StoreViewModel
import com.martabak.ecommerce.main.store.data.ProductQuery
import com.martabak.ecommerce.network.data.ResultData
import com.martabak.ecommerce.repository.StoreRepository
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
class StoreViewModelTest {
    private lateinit var storeModel: StoreViewModel
    private lateinit var storeRepo : StoreRepository


    @Before
    fun setup() {
        storeRepo = mock()
        storeModel = StoreViewModel(
            storeRepository = storeRepo,
            analytics = mock(),
            apiService = mock(),
            globalState = mock(),
            productRepository = mock()
        )
    }

    @Test
    fun updateFilterChipTest() {
        val expected = listOf("asrock", "gtx 1650")
        storeModel.updateFilterChipList(expected)
        assertEquals(expected, storeModel.filterChips.getOrAwaitValue())
    }

    @Test
    fun getSearchItemsSuccess() = runTest {
        val successResult = ResultData<List<String>>("ok", true, 200, listOf())
        whenever(storeRepo.getSearchItems("asus")).thenReturn(successResult)
        storeModel.getSearchItems("asus")
        assertEquals(listOf<String>(), storeModel.items.getOrAwaitValue())

    }

    @Test
    fun setSearchTest() {
        val expected = ProductQuery("asus")
        storeModel.setSearch("asus")
        assertEquals(expected, storeModel.queryObject.getOrAwaitValue())
        storeModel.setSearch("")
    }

    @Test
    fun setFiltersTest() {
        val expected = ProductQuery(
            search = "",
            brand = "asus",
            lowest = null,
            highest = null,
            sort = null
        )
        storeModel.setFilters("asus", null, null, null)
        assertEquals(expected, storeModel.queryObject.getOrAwaitValue())
        storeModel.setFilters(null, null, null, null)
    }





}