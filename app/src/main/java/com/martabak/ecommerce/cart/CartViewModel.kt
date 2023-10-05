package com.martabak.ecommerce.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.martabak.core.network.data.checkout.CheckoutList
import com.martabak.core.database.CartEntity
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.repository.CartRepository
import com.martabak.ecommerce.repository.ProductRepository
import com.martabak.ecommerce.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    val cartRepository: CartRepository,
    val analytics: FirebaseAnalytics,
    val productRepo : ProductRepository
) : ViewModel() {
    var liveCartItemsList: LiveData<List<CartEntity>>? = cartRepository.updatedCartItems

    var someChecked = liveCartItemsList?.switchMap { items ->
        anItemSelected(items)
    }

    private fun anItemSelected(list: List<CartEntity>): LiveData<Boolean> {
        var checkeds = list.filter { it.isSelected }
        if (checkeds.isEmpty()) {
            return MutableLiveData(false)
        } else {
            return MutableLiveData(true)
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            cartRepository.deleteItem(id)
        }
    }

    fun setSelectedProductId(id : String) {
        productRepo.selectedProductID = id
    }

    fun deleteSelected() {
        viewModelScope.launch {
            cartRepository.deleteSelected()
        }
    }

    fun uncheckAll() {
        viewModelScope.launch {
            cartRepository.uncheckAllItem()
        }
    }

    fun checkAll() {
        viewModelScope.launch {
            cartRepository.checkAllItem()
        }
    }

    fun selectItem(id: String, check: Boolean) {
        viewModelScope.launch {
            cartRepository.selectItem(id, check)
        }
    }

    fun addItem(id: String) {
        viewModelScope.launch {
            cartRepository.addItem(id)
        }
    }

    fun substractItem(id: String) {
        viewModelScope.launch {
            cartRepository.substractItem(id)
        }
    }

    // function to convert list
    fun parcelizeCartList(): CheckoutList {
        val entityList = liveCartItemsList?.value!!.filter { it.isSelected }
        val checkoutList = entityList!!.map { cart ->
            CheckoutData(
                productStock = cart.productStock,
                productVariant = cart.productVariant,
                productName = cart.productName,
                productQuantity = cart.productQuantity,
                productImage = cart.productImage,
                productPrice = cart.productPrice,
                item_id = cart.item_id
            )
        }
        return CheckoutList(checkoutList)
    }
}
