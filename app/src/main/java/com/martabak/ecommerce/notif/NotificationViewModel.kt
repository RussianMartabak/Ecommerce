package com.martabak.ecommerce.notif

import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.database.NotifDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notifDao: NotifDao) : ViewModel() {
    val updatedNotif = notifDao.getAllNotifs()
}