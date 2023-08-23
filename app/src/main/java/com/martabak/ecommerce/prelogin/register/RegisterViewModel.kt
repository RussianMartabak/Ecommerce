package com.martabak.ecommerce.prelogin.register

import android.content.SharedPreferences
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.registerBody
import com.martabak.ecommerce.network.data.RegisterResponse
import com.martabak.ecommerce.repository.UserRepository
import com.martabak.ecommerce.utils.SharedPrefKeys.login
import com.martabak.ecommerce.utils.SharedPrefKeys.putAccessToken
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

    var errorMessage: String = ""

    private var _validity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var validity: LiveData<Boolean> = _validity

    fun register() {
        val body = registerBody(email!!, "", password!!)
        viewModelScope.launch {
            try {
                val response = apiService.postRegister(body)
                userPref.login()
                storeTokens(response)
                userRepository.registerEntry() // should be moved to repository but thats later
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

    private fun storeTokens(responseBody: RegisterResponse) {
        //put access token
        userPref.putAccessToken(responseBody.data.accessToken)
    }
}