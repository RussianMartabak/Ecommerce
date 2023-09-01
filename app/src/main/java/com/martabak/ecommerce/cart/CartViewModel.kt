package com.martabak.ecommerce.cart

import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val cartRepository: CartRepository) : ViewModel() {
    var liveCartItemsList = cartRepository.updatedCartItems
}