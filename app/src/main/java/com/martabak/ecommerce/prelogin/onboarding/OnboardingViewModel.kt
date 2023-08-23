package com.martabak.ecommerce.prelogin.onboarding

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.utils.SharedPrefKeys.isFirstTime
import com.martabak.ecommerce.utils.SharedPrefKeys.registerEntry

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(val userPref: SharedPreferences) : ViewModel() {
    val isFirst = userPref.isFirstTime()

    fun registerInstall() {
        userPref.registerEntry()
    }
}