package com.martabak.ecommerce.notif

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.core.database.NotifDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notifDao: NotifDao) : ViewModel() {
    val updatedNotif = notifDao.getAllNotifs()

    fun updateNotif(id: Int, read: Boolean) {
        viewModelScope.launch {
            notifDao.setNotifAsRead(id, read)
        }
    }
}
