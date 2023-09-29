package com.martabak.ecommerce.preferenceTest

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.martabak.ecommerce.utils.GlobalUtils.getBearerToken
import com.martabak.ecommerce.utils.GlobalUtils.getRefreshToken
import com.martabak.ecommerce.utils.GlobalUtils.getUsername
import com.martabak.ecommerce.utils.GlobalUtils.isFirstTime
import com.martabak.ecommerce.utils.GlobalUtils.isLoggedIn
import com.martabak.ecommerce.utils.GlobalUtils.login
import com.martabak.ecommerce.utils.GlobalUtils.nightMode
import com.martabak.ecommerce.utils.GlobalUtils.putAccessToken
import com.martabak.ecommerce.utils.GlobalUtils.registerEntry
import com.martabak.ecommerce.utils.GlobalUtils.setNightMode
import com.martabak.ecommerce.utils.GlobalUtils.setRefreshToken
import com.martabak.ecommerce.utils.GlobalUtils.setUsername
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SharedPreferencesTest {
    private lateinit var context: Context
    private lateinit var userPref: SharedPreferences

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        userPref = context.getSharedPreferences("testPrefData", Context.MODE_PRIVATE)
    }

    @Test
    fun registerEntryTest() {
        userPref.registerEntry()
        assertEquals(false, userPref.isFirstTime())
    }

    @Test
    fun saveAccessTokenTest() {
        userPref.putAccessToken("hoshino")
        assertEquals("Bearer hoshino", userPref.getBearerToken())
    }

    @Test
    fun saveLoginStateTest() {
        userPref.login()
        assertEquals(true, userPref.isLoggedIn())
    }

    @Test
    fun setUsernameTest() {
        userPref.setUsername("星野")
        assertEquals("星野", userPref.getUsername())
    }

    @Test
    fun saveRefreshTokenTest() {
        userPref.setRefreshToken("KCIC WHOOSH")
        assertEquals("KCIC WHOOSH", userPref.getRefreshToken())
    }

    @Test
    fun saveNightModeOnOrOffTest() {
        userPref.setNightMode(true)
        assertEquals(true, userPref.nightMode())
    }
}
