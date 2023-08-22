package com.martabak.ecommerce.profile

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.network.backendApiService
import com.martabak.ecommerce.utils.SharedPrefKeys.setUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val apiService: backendApiService,
    val userPref: SharedPreferences
) : ViewModel() {

    private var _connectSuccess: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var selectedFile: File? = null
    var errorMessage = ""

    var connectSuccess: LiveData<Boolean> = _connectSuccess

    fun uploadProfile(username: String) {
        viewModelScope.launch {
            var userName = MultipartBody.Part
                .createFormData("userName", username)
            var userImage = MultipartBody.Part
                .createFormData("userImage", selectedFile!!.name, selectedFile!!.asRequestBody())
            try {
                val response = apiService.createProfile(userName, userImage)
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