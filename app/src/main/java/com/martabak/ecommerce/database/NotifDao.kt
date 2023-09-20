package com.martabak.ecommerce.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotifDao {

    @Insert
    suspend fun insertNotif(item : NotifEntity)

    //count
    @Query("SELECT COUNT(*) FROM notif")
    fun getCount() : LiveData<Int>

    @Query("SELECT * FROM notif")
    fun getAllNotifs() : LiveData<List<NotifEntity>>
}