package com.martabak.ecommerce.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.database.NotifDao
import com.martabak.ecommerce.repository.CartRepository
import com.martabak.ecommerce.repository.WishlistRepository
import com.martabak.ecommerce.utils.GlobalUtils.clearUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository,
    val userPref: SharedPreferences,
    private val notifDao: NotifDao
) : ViewModel() {

    val wishItemCount = wishlistRepository.itemCount
    val cartItemCount = cartRepository.updatedItemCount

    val updatedNotifCount = notifDao.getCount()

}