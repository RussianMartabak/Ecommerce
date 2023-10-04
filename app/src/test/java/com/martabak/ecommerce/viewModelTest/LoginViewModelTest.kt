package com.martabak.ecommerce.viewModelTest


import com.martabak.core.network.ApiService
import com.martabak.core.network.data.prelogin.LoginBody
import com.martabak.core.network.data.prelogin.dataLogin
import com.martabak.core.network.data.prelogin.loginResponse
import com.martabak.ecommerce.prelogin.login.LoginViewModel
import com.martabak.ecommerce.util.Extensions.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {
    private lateinit var loginModel: LoginViewModel
    private lateinit var mockApi: ApiService

    @Before
    fun setup() {
        mockApi = mock()
        loginModel = LoginViewModel(apiService = mockApi, analytics = mock(), userPref = mock())
        loginModel.email = ""
        loginModel.password = ""
    }

    @Test
    fun LoginSuccessTest() = runTest {
        val response = loginResponse(
            code = 200,
            message = "OK",
            dataLogin("", "", expiresAt = 600, userImage = "", userName = "")
        )
        whenever(mockApi.postLogin(LoginBody("", "", ""))).thenReturn(response)
        loginModel.Login()
        assertEquals(true, loginModel.serverValidity.getOrAwaitValue())
    }

    @Test
    fun LoginErrorTest() = runTest {
        whenever(mockApi.postLogin(LoginBody("", "", ""))).thenThrow(RuntimeException("test error"))
        loginModel.Login()
        assertEquals(false, loginModel.serverValidity.getOrAwaitValue())
    }

    @Test
    fun validateEmailValidTest() {
        assertEquals(true, loginModel.validateEmail("zaky@gmail.com"))
    }

    @Test
    fun validateEmailInvalidTest() {
        assertEquals(false, loginModel.validateEmail("zaky"))
    }

    @Test
    fun validatePasswordValidTest() {
        assertEquals(true, loginModel.validatePassword("12345678"))
    }

    @Test
    fun validatePasswordInvalidTest() {
        assertEquals(false, loginModel.validatePassword("as"))
    }
}
