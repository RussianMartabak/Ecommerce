package com.martabak.ecommerce.roomTest

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.martabak.ecommerce.database.AppDatabase
import com.martabak.ecommerce.database.CartDao
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.util.Extensions.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartDaoTest {
    private lateinit var cartDao: CartDao
    private lateinit var db: AppDatabase
    private lateinit var dbItem: CartEntity

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runTest {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        cartDao = db.cartDao()
        dbItem = CartEntity(
            item_id = "lol",
            productName = "i3 12100F",
            productVariant = "8",
            productStock = 1,
            isSelected = false,
            productImage = "1945.jpg",
            productPrice = 100,
            productQuantity = 2
        )
        cartDao.insertItem(dbItem)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun saveCartItemTest() = runTest {
        assertEquals(dbItem, cartDao.findItembyId("lol"))
    }

    @Test
    fun addItemQuantityTest() = runTest {
        cartDao.addItemCount("lol")
        assertEquals(3, cartDao.findItembyId("lol")?.productQuantity)
        cartDao.substractItem("lol")
    }

    @Test
    fun substractItemQuantityTest() = runTest {
        cartDao.substractItem("lol")
        assertEquals(1, cartDao.findItembyId("lol")?.productQuantity)
        cartDao.addItemCount("lol")
    }

    @Test
    fun checkCartItemTest() = runTest {
        cartDao.selectItem("lol", true)
        assertEquals(true, cartDao.findItembyId("lol")?.isSelected)
        cartDao.selectItem("lol", false)
        // reset the state again
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllCartItemTest() = runTest {
        val livedata = cartDao.getAll()
        advanceUntilIdle()
        var actual = livedata.getOrAwaitValue()
        assertEquals(listOf(dbItem), actual)
    }

    @Test
    fun getCartItemCountTest() = runTest {
        assertEquals(1, cartDao.getItemCount().getOrAwaitValue())
    }

    @Test
    fun deleteCartItemTest() = runTest {
        cartDao.deleteItem("lol")
        assertEquals(null, cartDao.findItembyId("lol"))
        cartDao.insertItem(dbItem)
    }

    @Test
    fun getCartItemCount() = runTest {
        assertEquals(1, cartDao.getItemCount().getOrAwaitValue())
    }
}
