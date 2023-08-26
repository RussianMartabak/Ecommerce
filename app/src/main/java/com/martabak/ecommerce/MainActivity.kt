package com.martabak.ecommerce

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.rootNavHost) as NavHostFragment
    }
    private val navController by lazy {navHostFragment.navController}
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    }

    fun logout() {
        navController.navigate(R.id.action_to_postlogin_prelogin)
    }

}