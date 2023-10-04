package com.martabak.ecommerce.repository

import com.martabak.core.database.WishlistDao
import com.martabak.core.database.WishlistEntity
import javax.inject.Inject

class WishlistRepository @Inject constructor(val wishlistDao: WishlistDao) {
    suspend fun insertItem(item: WishlistEntity) {
        wishlistDao.insertItem(item)
    }

    val itemCount = wishlistDao.getCount()

    suspend fun deleteItemById(id: String) {
        wishlistDao.deleteItem(id)
    }

    suspend fun itemExistOnWishlist(id: String): Boolean {
        return wishlistDao.findItemById(id) != null
    }

    var wishItems = wishlistDao.getAll()
}
