package com.martabak.ecommerce.prelogin

import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.network.backendApiService
import com.martabak.ecommerce.network.data.registerBody
import com.martabak.ecommerce.network.data.registerResponse
import com.martabak.ecommerce.utils.SharedPrefKeys.login
import com.martabak.ecommerce.utils.SharedPrefKeys.putAccessToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val apiService: backendApiService,
    val userPref: SharedPreferences
) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var errorMessage: String = ""
    private var _validity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    var validity: LiveData<Boolean> = _validity
    fun register() {
        var body = registerBody(email!!, "", password!!)
        viewModelScope.launch {
            try {
                val response = apiService.postRegister(body)
                userPref.login()
                storeTokens(response)
                _validity.value = true
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        errorMessage = "Email already taken"
                    }
                } else {
                    errorMessage = "${e.message}"
                }
                _validity.value = false
            }
        }
    }

    private fun storeTokens(responseBody: registerResponse) {
        //put access token
        userPref.putAccessToken(responseBody.data.accessToken)
    }

    fun validateEmail(emailInput: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
    }

    fun validatePassword(passInput: String): Boolean {
        return passInput.length >= 8
    }


}