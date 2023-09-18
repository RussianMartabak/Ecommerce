package com.martabak.ecommerce.prelogin.register

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.prelogin.RegisterResponse
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
    val userRepository: UserRepository
) : ViewModel() {


    var email: String? = null
    var password: String? = null
    var firebaseToken : String = ""

    var errorMessage: String = ""

    private var _validity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var connectionStatus: LiveData<Boolean> = _validity

    private var _nowLoading : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var nowLoading : LiveData<Boolean> = _nowLoading

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            firebaseToken = it.result
            Log.d("zaky", "FB Token: $firebaseToken")
        })
    }

    fun register() {
        viewModelScope.launch {
            try {
                _nowLoading.value = true
                val response = userRepository.register(email!!, password!!, firebaseToken)
                _validity.value = true
                _nowLoading.value = false
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        errorMessage = "Email already taken"
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
        //put access token
        userPref.putAccessToken(responseBody.data.accessToken)
    }
}