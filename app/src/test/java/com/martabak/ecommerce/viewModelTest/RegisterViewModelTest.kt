package com.martabak.ecommerce.viewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.martabak.ecommerce.prelogin.register.RegisterViewModel
import com.martabak.ecommerce.repository.UserRepository
import com.martabak.ecommerce.util.Extensions.getOrAwaitValue
import com.martabak.ecommerce.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegisterViewModelTest {
    private lateinit var registerModel: RegisterViewModel
    private lateinit var userRepo: UserRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @JvmField
    @Rule
    val jaRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        userRepo = mock()
        registerModel = RegisterViewModel(
            apiService = mock(),
            analytics = mock(),
            userPref = mock(),
            userRepository = userRepo
        )
        registerModel.email = ""
        registerModel.password = ""
    }

    @Test
    fun registerSuccessTest() = runTest {
        whenever(userRepo.register("", "", "")).thenReturn(true)
        registerModel.register()
        assertEquals(true, registerModel.connectionStatus.getOrAwaitValue())
    }

    @Test
    fun registerErrorTest() = runTest {
        whenever(userRepo.register("", "", "")).thenThrow(RuntimeException("test error"))
        registerModel.register()
        assertEquals(false, registerModel.connectionStatus.getOrAwaitValue())
    }
}
