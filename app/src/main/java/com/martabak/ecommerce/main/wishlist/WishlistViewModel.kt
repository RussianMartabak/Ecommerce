package com.martabak.ecommerce.main.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.database.WishlistEntity
import com.martabak.ecommerce.repository.CartRepository
import com.martabak.ecommerce.repository.ProductRepository
import com.martabak.ecommerce.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) :
    ViewModel() {
    val itemCount = wishlistRepository.itemCount
    val wishItems = wishlistRepository.wishItems

    private val eventChannel = Channel<String>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun setSelectedId(id: String) {
        productRepository.selectedProductID = id
    }

    fun insertItem(item: WishlistEntity) {
        val newCartEntity = CartEntity(
            isSelected = false,
            item_id = item.item_id,
            productPrice = item.productPrice,
            productImage = item.productImage,
            productName = item.productName,
            productQuantity = 1,
            productStock = item.productStock,
            productVariant = item.productVariant
        )
        viewModelScope.launch {
            try {
                cartRepository.insertProductData(newCartEntity)
                sendSnackbar("Item added to cart")
            } catch (e: Throwable) {
                sendSnackbar(e.message!!)
            }
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            wishlistRepository.deleteItemById(id)
            sendSnackbar("Item removed from wishlist")
        }
    }

    private fun sendSnackbar(s: String) {
        viewModelScope.launch {
            eventChannel.send(s)
        }
    }
}
