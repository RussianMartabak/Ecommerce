package com.martabak.ecommerce.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(


    val UserRep: UserRepository
) : ViewModel() {

    private var _connectSuccess: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var connectSuccess: LiveData<Boolean> = _connectSuccess

    private var _nowLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var nowLoading : LiveData<Boolean> = _nowLoading

    var selectedFile: File? = null
    var errorMessage = ""

    fun hasUsername() : Boolean {
        return UserRep.hasUsername()
    }

    fun uploadProfile(username: String) {
        viewModelScope.launch {
            _nowLoading.value = true
            val result = UserRep.uploadProfile(username, selectedFile)
            errorMessage = result.message
            _connectSuccess.value = result.success
            _nowLoading.value = false
        }
    }
}