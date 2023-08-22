package com.martabak.ecommerce.profile

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.utils.SharedPrefKeys.setUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val apiService: ApiService,
    val userPref: SharedPreferences
) : ViewModel() {

    private var _connectSuccess: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var connectSuccess: LiveData<Boolean> = _connectSuccess

    var selectedFile: File? = null
    var errorMessage = ""

    fun uploadProfile(username: String) {
        viewModelScope.launch {
            val userName = MultipartBody.Part
                .createFormData("userName", username)
            val userImage = MultipartBody.Part
                .createFormData("userImage", selectedFile!!.name, selectedFile!!.asRequestBody())
            try {
                apiService.createProfile(userName, userImage)
                userPref.setUsername(username)
                _connectSuccess.value = true
            } catch (e: Exception) {
                Log.d("zaky", "Upload Profile post exception: ${e.message}")
                errorMessage = e.message!!
                _connectSuccess.value = false
            }
        }
    }
}