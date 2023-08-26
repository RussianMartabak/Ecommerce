package com.martabak.ecommerce.main.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.martabak.ecommerce.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(val storeRepository: StoreRepository) : ViewModel() {

    var searchQuery = storeRepository.liveSearchKey
    var filterChips = storeRepository.filterChips

    val pagerFlow = Pager(
        PagingConfig(pageSize = 6)
    ) {
        ProductsPagingSource(storeRepository)
    }.flow.cachedIn(viewModelScope)
}