package com.martabak.ecommerce.viewModelTest

import com.martabak.ecommerce.main.wishlist.WishlistViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WishlistViewModelTest {
    private lateinit var wishModel: WishlistViewModel

    @Before
    fun setup() {
        wishModel = WishlistViewModel(
            cartRepository = mock(),
            productRepository = mock(),
            wishlistRepository = mock()
        )
    }

    @Test
    fun sendFlowTest() = runTest {
        wishModel.deleteItem("lol")
        assertEquals("Item removed from wishlist", wishModel.eventFlow.first())
    }


}
