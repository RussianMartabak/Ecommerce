package com.martabak.ecommerce.main.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.utils.GlobalUtils.clearUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val userPref: SharedPreferences) : ViewModel() {

    fun logout() {
        userPref.clearUserData()
    }
}