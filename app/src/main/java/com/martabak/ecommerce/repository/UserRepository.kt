package com.martabak.ecommerce.repository

import android.content.SharedPreferences
import android.util.Log
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.ResultData
import com.martabak.ecommerce.utils.SharedPrefKeys.hasUsername
import com.martabak.ecommerce.utils.SharedPrefKeys.isFirstTime
import com.martabak.ecommerce.utils.SharedPrefKeys.registerEntry
import com.martabak.ecommerce.utils.SharedPrefKeys.setUsername
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userPref: SharedPreferences,
    private val apiService: ApiService
) {
    var hasUsername = userPref.hasUsername()

    var firstEntry = userPref.isFirstTime()
    fun registerEntry() {
        userPref.registerEntry()
    }

    suspend fun uploadProfile(username: String, selectedFile: File) : ResultData {
        val userName = MultipartBody.Part
            .createFormData("userName", username)
        val userImage = MultipartBody.Part
            .createFormData("userImage", selectedFile.name, selectedFile.asRequestBody())
        try {
            apiService.createProfile(userName, userImage)
            userPref.setUsername(username)
            return ResultData("", true)
        } catch (e: Exception) {
            Log.d("zaky", "Upload Profile post exception: ${e.message}")
            val errorMessage = e.message!!
            return ResultData(errorMessage, false)
        }
    }

}