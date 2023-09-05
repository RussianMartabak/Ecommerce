package com.martabak.ecommerce.main

import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.repository.CartRepository
import com.martabak.ecommerce.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    val wishItemCount = wishlistRepository.itemCount
    val cartItemCount = cartRepository.updatedItemCount
}