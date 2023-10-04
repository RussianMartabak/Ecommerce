package com.martabak.ecommerce.product_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.core.network.data.product_detail.ReviewData
import com.martabak.ecommerce.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(val productRepository: ProductRepository) : ViewModel() {

    private var _reviewData = MutableLiveData<List<ReviewData>>()
    var reviewData: LiveData<List<ReviewData>> = _reviewData

    fun getReviews() {
        viewModelScope.launch {
            try {
                val reviews = productRepository.getProductReviews()
                _reviewData.value = reviews
            } catch (e: Throwable) {
            }
        }
    }
}
