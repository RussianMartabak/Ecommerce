package com.martabak.ecommerce

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.core.util.GlobalState
import com.martabak.core.database.AppDatabase
import com.martabak.ecommerce.utils.GlobalUtils.clearUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val globalState: GlobalState,
    private val userPref: SharedPreferences,
    private val db: AppDatabase
) : ViewModel() {
    val logoutFlow = globalState.logoutEventFlow

    fun logout() {
        userPref.clearUserData()
        viewModelScope.launch(Dispatchers.IO) {
            db.clearAllTables()
        }
    }
}
