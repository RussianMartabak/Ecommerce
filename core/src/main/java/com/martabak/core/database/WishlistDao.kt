package com.martabak.core.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WishlistDao {

    @Insert
    suspend fun insertItem(item: WishlistEntity)

    @Query("SELECT * FROM wishlist WHERE item_id = :id")
    suspend fun findItemById(id: String): WishlistEntity?

    @Query("SELECT * FROM wishlist")
    fun getAll(): LiveData<List<WishlistEntity>>

    @Query("SELECT COUNT(*) FROM wishlist")
    fun getCount(): LiveData<Int>

    @Query("DELETE FROM wishlist WHERE item_id = :id")
    suspend fun deleteItem(id: String)
}
