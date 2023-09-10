package com.martabak.ecommerce

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
        }
    }


}