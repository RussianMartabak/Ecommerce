package com.martabak.ecommerce.main.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.utils.SharedPrefKeys.logout
import com.martabak.ecommerce.utils.SharedPrefKeys.putAccessToken
import com.martabak.ecommerce.utils.SharedPrefKeys.setUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val userPref : SharedPreferences) : ViewModel() {

    fun logout() {
        userPref.putAccessToken("")
        userPref.setUsername("")
        userPref.logout()
    }
}