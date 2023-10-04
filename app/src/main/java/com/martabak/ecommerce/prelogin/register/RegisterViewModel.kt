package com.martabak.ecommerce.prelogin.register

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.messaging.FirebaseMessaging
import com.martabak.core.network.ApiService
import com.martabak.core.network.data.prelogin.RegisterResponse
import com.martabak.ecommerce.repository.UserRepository
import com.martabak.ecommerce.utils.GlobalUtils.putAccessToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val apiService: ApiService,
    val userPref: SharedPreferences,
    val userRepository: UserRepository,
    val analytics: FirebaseAnalytics
) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var firebaseToken: String = ""

    var errorMessage: String = ""

    private var _validity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var connectionStatus: LiveData<Boolean> = _validity

    private var _nowLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var nowLoading: LiveData<Boolean> = _nowLoading

    init {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener {
                    firebaseToken = it.result
                    Log.d("zaky", "FB Token: $firebaseToken")
                }
            )
        } catch (e: Throwable) {
        }
    }

    fun register() {
        viewModelScope.launch {
            try {
                _nowLoading.value = true
                val response = userRepository.register(email!!, password!!, firebaseToken)
                analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                    param(FirebaseAnalytics.Param.METHOD, email!!)
                }
                _validity.value = true
                _nowLoading.value = false
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        errorMessage = "Email already taken"
                    }
                    analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                        param(FirebaseAnalytics.Param.METHOD, email!!)
                    }
                } else {
                    errorMessage = "${e.message}"
                }
                _nowLoading.value = false
                _validity.value = false
            }
        }
    }

    private fun storeTokens(responseBody: RegisterResponse) {
        // put access token
        userPref.putAccessToken(responseBody.data.accessToken)
    }
}
