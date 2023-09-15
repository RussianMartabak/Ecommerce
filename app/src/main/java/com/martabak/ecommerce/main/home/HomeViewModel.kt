package com.martabak.ecommerce.main.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.GlobalState
import com.martabak.ecommerce.utils.GlobalUtils.clearUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val userPref: SharedPreferences,
    val globalState: GlobalState,
    val sharedPreferences: SharedPreferences
) : ViewModel() {

    fun isUserProductViewing() : Boolean {
        return globalState.inProductDetail
    }

    fun logout() {
        userPref.clearUserData()
    }
}