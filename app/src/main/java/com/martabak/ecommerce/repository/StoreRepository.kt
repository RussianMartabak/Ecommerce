package com.martabak.ecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.ResultData
import javax.inject.Inject

class StoreRepository @Inject constructor(val apiService : ApiService) {

    suspend fun getSearchItems(query : String) : ResultData<List<String>> {
        try {
            val response = apiService.postSearch(query)
            return ResultData("OK", true, 200, response.data)
        } catch(e : Throwable) {
            return ResultData(e.message!!, false, 69)
        }
    }

    //filter arrays
    private var _filters : MutableLiveData<MutableList<Any>> = MutableLiveData<MutableList<Any>>()
    var filters : LiveData<MutableList<Any>> = _filters





}