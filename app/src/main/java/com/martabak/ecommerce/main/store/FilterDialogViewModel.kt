package com.martabak.ecommerce.main.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterDialogViewModel @Inject constructor(val storeRepository: StoreRepository) :
    ViewModel() {

    fun sendProductQuery(brand: String?, lowest: Int?, highest: Int?, sort: String?) {
        storeRepository.setFilters(brand, lowest, highest, sort)
        viewModelScope.launch {
            storeRepository.getProducts()
        }
    }

}