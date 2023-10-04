package com.martabak.ecommerce.repositoryTest

import com.martabak.core.database.WishlistDao
import com.martabak.core.database.WishlistEntity
import com.martabak.ecommerce.repository.WishlistRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class WishlistRepoTest {
    private lateinit var wishlistRepo: WishlistRepository
    private lateinit var mockdDao: WishlistDao

    @Before
    fun setup() {
        mockdDao = mock()
        wishlistRepo = WishlistRepository(mockdDao)
    }

    @Test
    fun insertItemTest() = runTest {
        assertEquals(
            Unit,
            wishlistRepo.insertItem(WishlistEntity("", "", 100, "", "", 4.5, 100, "", 0))
        )
    }

    @Test
    fun deleteItemByIdTest() = runTest {
        assertEquals(Unit, wishlistRepo.deleteItemById("lol"))
    }

    @Test
    fun itemExistOnWishlistTest() = runTest {
        whenever(mockdDao.findItemById("lol")).thenReturn(null)
        assertEquals(false, wishlistRepo.itemExistOnWishlist("lol"))
    }
}
