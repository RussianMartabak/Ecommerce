package com.martabak.ecommerce.product_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(val productRepository: ProductRepository) : ViewModel() {


    fun getReviews() {
        viewModelScope.launch {
            try {
                val reviews = productRepository.getProductReviews()

            } catch(e : Throwable) {
                Log.d("zaky", "Error $e")
            }
        }
    }
}