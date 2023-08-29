package com.martabak.ecommerce.main.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.martabak.ecommerce.main.store.data.ProductQuery
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    val storeRepository: StoreRepository,
    val apiService: ApiService
) : ViewModel() {
    var query = ""
    private var brand: String? = null
    var lowest: Int? = null
    var highest: Int? = null
    private var sort: String? = null

    //storing selected chip IDs
    var selectedBrand: Int? = null
    var selectedSort: Int? = null

    private var _queryObject: MutableLiveData<ProductQuery> = MutableLiveData<ProductQuery>()
    var queryObject: LiveData<ProductQuery> = _queryObject

    // need an empty query object
    init {
        _queryObject.value = ProductQuery()
    }

    val updatedPagingSource = queryObject.switchMap { productQuery ->
        storeRepository.getProductsPagingData(productQuery)

    }.cachedIn(viewModelScope)

    //observe/transform the query object


    private var _filterChips = MutableLiveData<List<String>>()
    var filterChips: LiveData<List<String>> = _filterChips

    fun updateFilterChipList(list: List<String>) {
        _filterChips.value = list
    }


    fun sendQuery(q: String) {

        viewModelScope.launch {
            storeRepository.getProducts()
        }
    }

    private var _nowLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var nowLoading: LiveData<Boolean> = _nowLoading

    private var _items: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    var items: LiveData<List<String>> = _items
    fun getSearchItems(query: String) {
        viewModelScope.launch {
            _nowLoading.value = true
            val response = storeRepository.getSearchItems(query)
            if (response.success) {
                _items.value = response.content!!
            }
            _nowLoading.value = false
        }
    }

    //function to tamper with the pagingsource
    fun setSearch(q: String) {
        query = q
        val newQuery =
            ProductQuery(search = q, brand = brand, lowest = lowest, highest = highest, sort = sort)
        _queryObject.value = newQuery
    }

    fun setFilters(_brand: String?, _lowest: Int?, _highest: Int?, _sort: String?) {
        brand = _brand
        lowest = _lowest
        highest = _highest
        sort = _sort
        val newQuery =
            ProductQuery(
                search = query,
                brand = brand,
                lowest = lowest,
                highest = highest,
                sort = sort
            )
        _queryObject.value = newQuery
    }
    fun callRefresh() {
        val newQuery = ProductQuery(
            search = query,
            brand = brand,
            lowest = lowest,
            highest = highest,
            sort = sort
        )
        _queryObject.value = newQuery
    }

    fun resetFilters() {
        brand = null
        sort = null
        selectedBrand = null
        selectedSort = null
        lowest = null
        highest = null
    }


}