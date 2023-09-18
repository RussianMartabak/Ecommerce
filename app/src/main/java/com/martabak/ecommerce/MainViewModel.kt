package com.martabak.ecommerce

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.utils.GlobalUtils.clearUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val globalState: GlobalState,
    private val userPref: SharedPreferences
) : ViewModel() {
    val logoutFlow = globalState.logoutEventFlow

    fun logout() {
        userPref.clearUserData()
    }
}