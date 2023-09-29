package com.martabak.ecommerce.repositoryTest

import android.content.SharedPreferences
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.ResultData
import com.martabak.ecommerce.network.data.prelogin.DataRegister
import com.martabak.ecommerce.network.data.prelogin.RegisterBody
import com.martabak.ecommerce.network.data.prelogin.RegisterResponse
import com.martabak.ecommerce.repository.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserRepoTest {
    private lateinit var userRepo: UserRepository
    private lateinit var mockSP: SharedPreferences
    private lateinit var mockApi: ApiService

    @Before
    fun setup() {
        mockSP = mock()
        mockApi = mock()
        userRepo = UserRepository(mockSP, mockApi)
    }

    @Test
    fun registerTest() = runTest {
        val body = RegisterBody("a", "a", "a")
        whenever(mockApi.postRegister(body)).thenReturn(
            RegisterResponse(
                200,
                "OK",
                DataRegister("a", "a", 600)
            )
        )
        assertEquals(true, userRepo.register("a", "a", "a"))
    }

    @Test
    fun uploadProfileTest() = runTest {
        val expected = ResultData<Int>("", true)
        assertEquals(expected, userRepo.uploadProfile("zaky", null))
    }
}
