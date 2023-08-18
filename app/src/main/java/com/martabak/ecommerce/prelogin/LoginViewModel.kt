package com.martabak.ecommerce.prelogin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.network.backendApiService
import com.martabak.ecommerce.network.data.loginBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val apiService : backendApiService) : ViewModel() {
    var emailValidity = false
    var passwordValidity = false
    var email : String? = null
    var password : String? = null
    var errorMessage = ""
    private var _serverValidity : MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    var serverValidity : LiveData<Boolean> = _serverValidity

    fun Login() {
        val body = loginBody(email!!, "", password!!)
        viewModelScope.launch {
            try {
                val response = apiService.postLogin(body)
                _serverValidity.value = true
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        errorMessage = "Wrong Email or Password"
                    }
                } else {
                    errorMessage = e.message.toString()
                }
                _serverValidity.value = false
            }
        }
    }
    fun validateEmail(emailInput : String) : Boolean {
        emailValidity = Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
        return emailValidity
    }

    fun validatePassword(passInput : String) : Boolean {
        passwordValidity = passInput.length >= 8
        return passwordValidity
    }

}