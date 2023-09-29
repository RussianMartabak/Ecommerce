package com.martabak.ecommerce.roomTest

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.martabak.ecommerce.database.AppDatabase
import com.martabak.ecommerce.database.WishlistDao
import com.martabak.ecommerce.database.WishlistEntity
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
class WishlistDaoTest {
    private lateinit var wishlistDao: WishlistDao
    private lateinit var db: AppDatabase
    private lateinit var dbItem: WishlistEntity

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runTest {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
            .build()
        wishlistDao = db.wishlistDao()
        dbItem = WishlistEntity(
            item_id = "lol",
            productName = "PC Gaming",
            productPrice = 100,
            productImage = "1965.jpg",
            productRating = 1.5,
            productSale = 11,
            productSeller = "EnterKomputer",
            productStock = 1,
            productVariant = "16GB"
        )
        wishlistDao.insertItem(dbItem)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun saveWishlistItemTest() = runTest {
        assertEquals(dbItem, wishlistDao.findItemById("lol"))
    }

    @Test
    fun getAllWishlistItemTest() = runTest {
        assertEquals(listOf(dbItem), wishlistDao.getAll().getOrAwaitValue())
    }

    @Test
    fun getWishlistItemCountTest() = runTest {
        assertEquals(1, wishlistDao.getCount().getOrAwaitValue())
    }

    @Test
    fun deleteWishlistItemTest() = runTest {
        wishlistDao.deleteItem("lol")
        assertEquals(null, wishlistDao.findItemById("lol"))
        wishlistDao.insertItem(dbItem)
    }
}
