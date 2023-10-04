package com.martabak.core.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GlobalState() {
    var inProductDetail = false
    private val logoutEvent = Channel<Boolean>()
    val logoutEventFlow = logoutEvent.receiveAsFlow()

    fun sendLogoutEvent() {
        GlobalScope.launch {
            logoutEvent.send(true)
            Log.d("zaky", "LOGOUT SHOULDVE HAPPENED")
        }
    }

    private val _remoteConfigDataString = MutableLiveData<String>()
    val remoteConfigDataString: LiveData<String> = _remoteConfigDataString

    fun updateRemoteConfig(msg: String) {
        _remoteConfigDataString.value = msg
    }
}
