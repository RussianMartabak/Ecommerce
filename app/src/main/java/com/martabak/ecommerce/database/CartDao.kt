package com.martabak.ecommerce.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    suspend fun insertItem(item: CartEntity)

    // get with name and variant
    @Query("SELECT * FROM cart WHERE item_id = :id")
    suspend fun findItembyId(id: String): CartEntity?

    // update the count to the item with given
    @Query("UPDATE cart SET product_qty = product_qty + 1 WHERE item_id = :id")
    suspend fun addItemCount(id: String)

    @Query("UPDATE cart SET product_qty = product_qty - 1 WHERE item_id = :id")
    suspend fun substractItem(id: String)

    @Query("SELECT * FROM cart")
    fun getAll(): LiveData<List<CartEntity>>

    @Query("UPDATE cart SET selected = :check WHERE item_id = :id")
    suspend fun selectItem(id: String, check: Boolean)

    @Query("UPDATE cart SET selected = 1")
    suspend fun checkAllItem()

    @Query("UPDATE cart SET selected = 0")
    suspend fun uncheckAllItem()

    @Query("DELETE FROM cart WHERE selected = 1")
    suspend fun deleteSelected()

    @Query("DELETE FROM cart WHERE item_id = :id")
    suspend fun deleteItem(id: String)

    @Query("SELECT COUNT(*) FROM cart")
    fun getItemCount(): LiveData<Int>
}
