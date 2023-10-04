package com.martabak.ecommerce.roomTest

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.martabak.core.database.AppDatabase
import com.martabak.core.database.NotifDao
import com.martabak.core.database.NotifEntity
import com.martabak.ecommerce.util.Extensions.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotifDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dbItem: NotifEntity
    private lateinit var notifDao: NotifDao

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runTest {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
            .build()
        notifDao = db.notifDao()
        dbItem = NotifEntity(
            notif_id = 1,
            title = "1998",
            body = "Respect",
            image = "requiem.jpg",
            type = "Transaction",
            datetime = "9/11"
        )
        notifDao.insertNotif(dbItem)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun saveNotifItemTest() {
        assertEquals(listOf(dbItem), notifDao.getAllNotifs().getOrAwaitValue())
    }

    @Test
    fun getNotifItemCountTest() {
        assertEquals(1, notifDao.getCount().getOrAwaitValue())
    }

    @Test
    fun setNotifAsReadTest() = runTest {
        notifDao.setNotifAsRead(1, true)
        assertEquals(true, notifDao.getAllNotifs().getOrAwaitValue().get(0).read)
    }
}
