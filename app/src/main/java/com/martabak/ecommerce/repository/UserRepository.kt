package com.martabak.ecommerce.repository

import android.content.SharedPreferences
import com.martabak.core.network.ApiService
import com.martabak.core.network.data.ResultData
import com.martabak.core.network.data.prelogin.RegisterBody
import com.martabak.ecommerce.utils.GlobalUtils.hasUsername
import com.martabak.ecommerce.utils.GlobalUtils.login
import com.martabak.ecommerce.utils.GlobalUtils.putAccessToken
import com.martabak.ecommerce.utils.GlobalUtils.setRefreshToken
import com.martabak.ecommerce.utils.GlobalUtils.setUsername
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userPref: SharedPreferences,
    private val apiService: ApiService
) {
    // marked for change
    fun hasUsername(): Boolean {
        return userPref.hasUsername()
    }

    suspend fun register(email: String, password: String, token: String): Boolean {
        val body = RegisterBody(email, password, token)
        try {
            val successfulResponse = apiService.postRegister(body)
            userPref.login()
            userPref.putAccessToken(successfulResponse.data.accessToken)
            userPref.setRefreshToken(successfulResponse.data.refreshToken)
            return true
        } catch (e: Throwable) {
            throw e
        }
    }

    suspend fun uploadProfile(username: String, selectedFile: File?): ResultData<Int> {
        val filename = selectedFile?.name
        val fileBody = selectedFile?.asRequestBody()
        val userName = MultipartBody.Part
            .createFormData("userName", username)
        val userImage = if (filename == null || fileBody == null) {
            null
        } else {
            MultipartBody.Part.createFormData("userImage", filename, fileBody)
        }
        try {
            apiService.createProfile(userName, userImage)
            userPref.setUsername(username)
            return ResultData<Int>("", true)
        } catch (e: Exception) {
            val errorMessage = e.message!!
            return ResultData<Int>(errorMessage, false)
        }
    }
}
