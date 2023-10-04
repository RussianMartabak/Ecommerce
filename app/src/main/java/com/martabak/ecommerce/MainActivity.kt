package com.martabak.ecommerce

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.martabak.ecommerce.utils.GlobalUtils.isFirstTime
import com.martabak.ecommerce.utils.GlobalUtils.nightMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.rootNavHost) as NavHostFragment
    }
    private val navController by lazy { navHostFragment.navController }
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig


    @Inject
    lateinit var sharedPreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val night =
            if (sharedPreference.nightMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(night)
        // subscribe channel
        Firebase.messaging.subscribeToTopic("promo").addOnCompleteListener { task ->
            var msg = "Subscribed"
            if (!task.isSuccessful) {
                msg = "Subscribe failed"
            }
            Log.d("zaky", msg)
        }

        //language
        val firstEntry = sharedPreference.isFirstTime()
        if (firstEntry) switchLang("en")

        lifecycleScope.launch {
            viewModel.logoutFlow.collectLatest { kick ->
                if (kick) logout()
            }
        }
    }

    fun simulateBack() {
        dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
    }

    fun logout() {
        viewModel.logout()
        navController.navigate(R.id.action_to_postlogin_prelogin)
    }

    fun switchLang(tag: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(tag)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun getLang(): String {
        val appLocale = AppCompatDelegate.getApplicationLocales()
        return appLocale.toLanguageTags()
    }

    fun setAppTheme(night: Boolean) {
        val nightMode =
            if (night) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
