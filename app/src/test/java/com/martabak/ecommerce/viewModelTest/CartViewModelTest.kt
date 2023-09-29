package com.martabak.ecommerce.viewModelTest

import androidx.lifecycle.MutableLiveData
import com.martabak.ecommerce.cart.CartViewModel
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.network.data.checkout.CheckoutList
import com.martabak.ecommerce.repository.CartRepository
import com.martabak.ecommerce.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class CartViewModelTest {
    private lateinit var cartModel: CartViewModel
    private lateinit var cartRepo: CartRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        cartRepo = mock()
        cartModel = CartViewModel(cartRepo, mock())
        cartModel.liveCartItemsList = MutableLiveData(
            listOf(
                CartEntity(
                    item_id = "lol",
                    productName = "i3 12100F",
                    productVariant = "8",
                    productStock = 1,
                    isSelected = true,
                    productImage = "1945.jpg",
                    productPrice = 100,
                    productQuantity = 2
                )
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteItemTest() = runTest {
        assertEquals(Unit, cartModel.deleteItem("lol"))
    }

    @Test
    fun deleteSelectedTest() {
        assertEquals(Unit, cartModel.deleteItem("lol"))
    }

    @Test
    fun uncheckAllTest() {
        assertEquals(Unit, cartModel.uncheckAll())
    }

    @Test
    fun checkAllTest() {
        assertEquals(Unit, cartModel.checkAll())
    }

    @Test
    fun selectItemTest() {
        assertEquals(Unit, cartModel.selectItem("lol", true))
    }

    @Test
    fun addItemTest() {
        assertEquals(Unit, cartModel.addItem("lol"))
    }

    @Test
    fun substractItem() {
        assertEquals(Unit, cartModel.substractItem("lol"))
    }

    @Test
    fun parcelizeCartListTest() {
        val expected = CheckoutList(
            listOf(
                CheckoutData(
                    item_id = "lol",
                    productName = "i3 12100F",
                    productVariant = "8",
                    productStock = 1,
                    productImage = "1945.jpg",
                    productPrice = 100,
                    productQuantity = 2
                )
            )
        )
        assertEquals(expected, cartModel.parcelizeCartList())
    }
}
