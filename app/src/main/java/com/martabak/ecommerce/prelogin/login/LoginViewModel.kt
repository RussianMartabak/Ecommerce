package com.martabak.ecommerce.prelogin.login

import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.messaging.FirebaseMessaging
import com.martabak.core.network.ApiService
import com.martabak.core.network.data.prelogin.LoginBody
import com.martabak.core.network.data.prelogin.loginResponse
import com.martabak.ecommerce.utils.GlobalUtils.isFirstTime
import com.martabak.ecommerce.utils.GlobalUtils.isLoggedIn
import com.martabak.ecommerce.utils.GlobalUtils.login
import com.martabak.ecommerce.utils.GlobalUtils.putAccessToken
import com.martabak.ecommerce.utils.GlobalUtils.setUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val apiService: ApiService,
    val userPref: SharedPreferences,
    val analytics: FirebaseAnalytics
) : ViewModel() {
    var emailValidity = false
    var passwordValidity = false
    var email: String? = null
    var password: String? = null
    var token: String = ""
    var errorMessage = ""
    private var _serverValidity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private var _nowLoading = MutableLiveData<Boolean>()
    var nowLoading: LiveData<Boolean> = _nowLoading

    var serverValidity: LiveData<Boolean> = _serverValidity
    var firstEntry = userPref.isFirstTime()

    init {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener {
                    token = it.result
                    Log.d("zaky", "FB Token: $token")
                }
            )
        } catch (e: Throwable) {
        }
    }

    fun isLoggedIn(): Boolean {
        return userPref.isLoggedIn()
    }

    fun Login() {
        val body = LoginBody(email!!, password!!, token)
        viewModelScope.launch {
            try {
                _nowLoading.value = true
                val response = apiService.postLogin(body)
                analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                    param(FirebaseAnalytics.Param.METHOD, email!!)
                }
                storeTokens(response)
                userPref.setUsername(response.data.userName)
                userPref.login()
                _nowLoading.value = false
                _serverValidity.value = true
            } catch (e: Exception) {
                _nowLoading.value = false
                if (e is HttpException) {
                    if (e.code() == 400) {
                        errorMessage = "Wrong Email or Password"
                    }
                    analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                        param(FirebaseAnalytics.Param.METHOD, email!!)
                    }
                } else {
                    errorMessage = e.message.toString()
                }
                Log.d("zaky", "Api Hit but failed")
                _serverValidity.value = false
            }
        }
    }

    fun validateEmail(emailInput: String): Boolean {
        emailValidity = Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
        email = emailInput
        return emailValidity
    }

    fun validatePassword(passInput: String): Boolean {
        passwordValidity = passInput.length >= 8
        password = passInput
        return passwordValidity
    }

    private fun storeTokens(responseBody: loginResponse) {
        // put access token
        userPref.putAccessToken(responseBody.data.accessToken)
    }
}
