package com.martabak.ecommerce.main.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDialogViewModel @Inject constructor(val storeRepository: StoreRepository) :
    ViewModel() {
    private var _nowLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var nowLoading: LiveData<Boolean> = _nowLoading

    private var _items: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    var items: LiveData<List<String>> = _items

    var searchQuery = storeRepository.liveSearchKey
    fun sendQuery(q : String) {
        storeRepository.setSearchQuery(q)
        viewModelScope.launch {
            storeRepository.getProducts()
        }
    }

    fun getSearchItems(query : String) {
        viewModelScope.launch {
            _nowLoading.value = true
            val response = storeRepository.getSearchItems(query)
            if (response.success) {
                _items.value = response.content!!
            }
            _nowLoading.value = false
        }
    }


}