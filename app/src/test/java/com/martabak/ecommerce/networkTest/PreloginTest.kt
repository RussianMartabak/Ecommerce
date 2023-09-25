package com.martabak.ecommerce.networkTest

import android.util.Log
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.prelogin.DataRegister
import com.martabak.ecommerce.network.data.prelogin.LoginBody
import com.martabak.ecommerce.network.data.prelogin.RefreshBody
import com.martabak.ecommerce.network.data.prelogin.RefreshData
import com.martabak.ecommerce.network.data.prelogin.RefreshResponse
import com.martabak.ecommerce.network.data.prelogin.RegisterBody
import com.martabak.ecommerce.network.data.prelogin.RegisterResponse
import com.martabak.ecommerce.network.data.prelogin.dataLogin
import com.martabak.ecommerce.network.data.prelogin.dataProfile
import com.martabak.ecommerce.network.data.prelogin.loginResponse
import com.martabak.ecommerce.network.data.prelogin.profileResponse
import com.martabak.ecommerce.networkTest.util.BasedNetworkTest
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.http.Multipart

class PreloginTest : BasedNetworkTest() {


    @Test
    fun loginApiTest() {
        mockwebServer.enqueueResponse("prelogin/LoginMockResponse.json")

        val loginBody: LoginBody = LoginBody("zaky@wh.us", "aa", "")
        val expected: loginResponse = loginResponse(200, "OK", dataLogin("123", "123", 600, "", ""))

        runBlocking {
            val actual = apiService.postLogin(loginBody)
            assertEquals(actual, expected)
        }
    }

    @Test
    fun registerApiTest() {
        mockwebServer.enqueueResponse("prelogin/RegisterMockResponse.json")

        //make a request
        val registerBody = RegisterBody("zaky@gmail.com", "lalalalalaa", "")
        val expected = RegisterResponse(
            code = 200,
            message = "OK",
            data = DataRegister(
                accessToken = "rotibakar",
                expiresAt = 600,
                refreshToken = "rotikukus"
            )
        )

        runBlocking {
            val actual = apiService.postRegister(registerBody)
            assertEquals(actual, expected)


        }

    }

    @Test
    fun refreshApiTest() {
        mockwebServer.enqueueResponse("prelogin/RefreshMockResponse.json")

        val refreshBody = RefreshBody("ui-beam")
        val expected = RefreshResponse(
            code = 200,
            message = "OK",
            data = RefreshData(accessToken = "shigure", refreshToken = "ui", expiresAt = 600)
        )

        runBlocking {
            val actual = apiService.postRefresh(refreshBody)
            assertEquals(actual, expected)
        }
    }

    @Test
    fun profileApiTest() {
        mockwebServer.enqueueResponse("prelogin/ProfileMockResponse.json")

        val expected =
            profileResponse(code = 200, message = "OK", dataProfile("unit test", "uibeam.png"))

        runBlocking {
            val actual = apiService.createProfile(
                MultipartBody.Part.createFormData("userName", "unit test"),
                null
            )
            assertEquals(actual, expected)
        }
    }

    @After
    fun tearDown() {
        mockwebServer.shutdown()
    }
}