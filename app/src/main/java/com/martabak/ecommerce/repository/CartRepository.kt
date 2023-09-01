package com.martabak.ecommerce.repository

import com.martabak.ecommerce.database.CartDao
import com.martabak.ecommerce.database.CartEntity
import javax.inject.Inject

class CartRepository @Inject constructor(private val cartDao: CartDao) {
    //le insert
    suspend fun insertProductData(data : CartEntity) {
        if (cartDao.findItembyId(data.item_id) == null) {
            //then just insert
            cartDao.insertItem(data)
        } else {
            cartDao.addItemCount(data.item_id)
        }

    }

    var updatedCartItems = cartDao.getAll()
}