package com.martabak.ecommerce.repositoryTest

import com.martabak.ecommerce.database.CartDao
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.repository.CartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CartRepoTest {
    private lateinit var cartRepo: CartRepository
    private lateinit var mockCartDao: CartDao
    private lateinit var leEntity: CartEntity

    @Before
    fun setup() {
        mockCartDao = mock()
        cartRepo = CartRepository(cartDao = mockCartDao)
        leEntity = CartEntity(
            item_id = "lol",
            productName = "i3 12100F",
            productVariant = "8",
            productStock = 1,
            isSelected = false,
            productImage = "1945.jpg",
            productPrice = 100,
            productQuantity = 2
        )
    }

    @Test
    fun insertProductDataTest() = runTest {
        whenever(mockCartDao.findItembyId("lol")).thenReturn(null)
        whenever(mockCartDao.insertItem(leEntity)).thenReturn(Unit)
        assertEquals(Unit, cartRepo.insertProductData(leEntity))
    }

    @Test
    fun addItemTest() = runTest {
        whenever(mockCartDao.findItembyId("lol")).thenReturn(leEntity)
        whenever(mockCartDao.addItemCount("lol")).thenReturn(Unit)
        assertEquals(Unit, cartRepo.addItem("lol"))
    }

    @Test
    fun checkAllItemTest() = runTest {
        assertEquals(Unit, cartRepo.checkAllItem())
    }

    @Test
    fun selectItemTest() = runTest {
        assertEquals(Unit, cartRepo.selectItem("lol", true))
    }

    @Test
    fun uncheckAllItemTest() = runTest {
        assertEquals(Unit, cartRepo.uncheckAllItem())
    }

    @Test
    fun deleteSelectedTest() = runTest {
        assertEquals(Unit, cartRepo.deleteSelected())
    }

    @Test
    fun deleteItemTest() = runTest {
        assertEquals(Unit, cartRepo.deleteItem("lol"))
    }

    @Test
    fun substractItemTest() = runTest {
        assertEquals(Unit, cartRepo.substractItem("lol"))
    }
}
