package com.martabak.ecommerce.main

import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val cartRepository: CartRepository) : ViewModel() {
    val cartItemCount = cartRepository.updatedItemCount
}