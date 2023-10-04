package com.martabak.ecommerce.repository

import com.martabak.core.database.CartDao
import com.martabak.core.database.CartEntity
import javax.inject.Inject

class CartRepository @Inject constructor(private val cartDao: CartDao) {
    // le insert
    suspend fun insertProductData(data: CartEntity) {
        var entity = cartDao.findItembyId(data.item_id)
        if (entity == null) {
            // then just insert
            cartDao.insertItem(data)
        } else if (entity.productStock == entity.productQuantity) {
            throw Exception("Out of Stock")
        } else {
            cartDao.addItemCount(data.item_id)
        }
    }
    suspend fun addItem(id: String) {
        val entity = cartDao.findItembyId(id)
        if (entity!!.productStock > entity.productQuantity) {
            cartDao.addItemCount(id)
        }
    }

    suspend fun checkAllItem() {
        cartDao.checkAllItem()
    }
    suspend fun selectItem(id: String, check: Boolean) {
        cartDao.selectItem(id, check)
    }

    suspend fun uncheckAllItem() {
        cartDao.uncheckAllItem()
    }

    suspend fun deleteSelected() {
        cartDao.deleteSelected()
    }

    suspend fun deleteItem(id: String) {
        cartDao.deleteItem(id)
    }

    suspend fun substractItem(id: String) {
        cartDao.substractItem(id)
    }

    val updatedItemCount = cartDao.getItemCount()
    var updatedCartItems = cartDao.getAll()
}
