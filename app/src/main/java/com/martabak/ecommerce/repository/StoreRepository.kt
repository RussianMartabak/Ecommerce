package com.martabak.ecommerce.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.ProductsResponse
import com.martabak.ecommerce.network.data.ResultData
import javax.inject.Inject

class StoreRepository @Inject constructor(val apiService: ApiService) {
    private val LIMIT = 6
    private val PAGE = 1
    suspend fun getSearchItems(query: String): ResultData<List<String>> {
        try {
            val response = apiService.postSearch(query)
            return ResultData("OK", true, 200, response.data)
        } catch (e: Throwable) {
            return ResultData(e.message!!, false, 69)
        }
    }


    //search word livedata
    private var _liveSearchKey: MutableLiveData<String> = MutableLiveData<String>()
    var liveSearchKey: LiveData<String> = _liveSearchKey

    var searchKey = ""

    fun setSearchQuery(q: String) {
        searchKey = q
        _liveSearchKey.value = q
    }

    fun setFilters(_brand: String?, _lowest: Int?, _highest: Int?, _sort: String?) {
        brand = _brand
        lowest = _lowest
        highest = _highest
        sort = _sort
        //delegate to function
        updateChipsList()
    }

    suspend fun getProducts(page : Int = 1) : ProductsResponse {
        try {
            val response =
                apiService.postProducts(searchKey, brand, lowest, highest, sort, LIMIT, page)
            return response
        } catch (e: Throwable) {
            Log.d("zaky", e.message!!)
            throw e

        }

    }

    // use this to notify viewmodels
    private var _productsPagingUpdateCycle : MutableLiveData<Int> = MutableLiveData<Int>()
    var productsPagingUpdateCycle : LiveData<Int> = _productsPagingUpdateCycle

    //function to construct array
    private fun updateChipsList() {
        val newFilterChips = mutableListOf<String>()
        if (brand != null) newFilterChips.add(brand!!)
        if (lowest != null) newFilterChips.add("> $lowest")
        if (highest != null) newFilterChips.add("< $highest")
        if (sort != null) newFilterChips.add(sort!!)
        _filterChips.value = newFilterChips
    }

    //array for creating filter chips
    private var _filterChips: MutableLiveData<MutableList<String>> =
        MutableLiveData<MutableList<String>>()
    var filterChips: LiveData<MutableList<String>> = _filterChips

    //filter variables
    var brand: String? = null
    var lowest: Int? = null
    var highest: Int? = null
    var sort: String? = null


}