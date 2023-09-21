package com.martabak.ecommerce.product_detail

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.martabak.ecommerce.GlobalState
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.database.WishlistEntity
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.network.data.checkout.CheckoutList
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.repository.CartRepository
import com.martabak.ecommerce.repository.ProductRepository
import com.martabak.ecommerce.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    val productRepository: ProductRepository,
    val globalState: GlobalState,
    val cartRepository: CartRepository,
    val wishlistRepository: WishlistRepository,
    private val analytics: FirebaseAnalytics
) :
    ViewModel() {
    private var _currentPrice = MutableLiveData<Int>()
    var currentPrice: LiveData<Int> = _currentPrice

    var selectedVariantIndex = 0

    private var basePrice: Int = 0


    //we need a boolean liveData
    private var _productOnWishlist = MutableLiveData<Boolean>()
    val productOnWishlist: LiveData<Boolean> = _productOnWishlist


    fun updateProductPrice(variantPrice: Int) {
        _productData.value!!.productPrice = basePrice + variantPrice
        _currentPrice.value = basePrice + variantPrice
    }

    //for notifying if action is completed
    private val eventChannel = Channel<String>()
    val eventFlow = eventChannel.receiveAsFlow()


    private var _connectionSuccess: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var connectionSuccess: LiveData<Boolean> = _connectionSuccess

    private var _productData: MutableLiveData<Data> = MutableLiveData<Data>()
    var productData: LiveData<Data> = _productData

    private var _nowLoading = MutableLiveData<Boolean>()
    val nowLoading: LiveData<Boolean> = _nowLoading


    var errorMessage = ""

    fun setProductID(id: String) {
        productRepository.selectedProductID = id
    }

    fun getProductData() {
        viewModelScope.launch {
            try {
                _nowLoading.value = true
                val response = productRepository.getProductDetail()
                Log.d("zaky", "response in viewmodel $response")
                _connectionSuccess.value = true
                _nowLoading.value = false
                _productData.value = response
                _productOnWishlist.value =
                    wishlistRepository.itemExistOnWishlist(_productData.value!!.productId)
                basePrice = response.productPrice
            } catch (e: Throwable) {
                Log.d("zaky", "product detail error $e")
                _connectionSuccess.value = false
                _nowLoading.value = false
            }
        }
    }

    fun triggerSnackbar(s: String) {
        viewModelScope.launch {
            eventChannel.send(s)
        }
    }

    fun processWishlist() {
        //check whether on wishlist or not
        val onWishlist = _productOnWishlist.value!!
        viewModelScope.launch {
            try {
                if (onWishlist) {
                    //remove
                    wishlistRepository.deleteItemById(_productData.value!!.productId)
                    _productOnWishlist.value = false
                    triggerSnackbar("Item removed from wishlist")
                } else {
                    //add
                    wishlistRepository.insertItem(makeWish())
                    _productOnWishlist.value = true
                    triggerSnackbar("Item added to wishlist")
                    //log event here
                    analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
                        param(
                            FirebaseAnalytics.Param.ITEMS,
                            arrayOf(bundleOf("name" to makeWish().productName))
                        )
                        param(FirebaseAnalytics.Param.CURRENCY, "IDR")
                        param(FirebaseAnalytics.Param.VALUE, makeWish().productPrice.toLong())
                    }

                }
            } catch (e: Throwable) {
                Log.d("zaky", "$e")
            }
        }
    }

    private fun makeWish(): WishlistEntity {
        return WishlistEntity(
            item_id = _productData.value!!.productId,
            productImage = _productData.value!!.image[0],
            productName = _productData.value!!.productName,
            productPrice = _productData.value!!.productPrice,
            productRating = _productData.value!!.productRating,
            productSale = _productData.value!!.sale,
            productSeller = _productData.value!!.store,
            productVariant = _productData.value!!.productVariant[selectedVariantIndex].variantName,
            productStock = _productData.value!!.stock
        )
    }


    fun addToCart() {
        val newEntity = CartEntity(
            productName = _productData.value!!.productName,
            productVariant = _productData.value!!.productVariant[selectedVariantIndex].variantName,
            productStock = _productData.value!!.stock,
            isSelected = false,
            productImage = _productData.value!!.image[0],
            productPrice = productData.value!!.productPrice,
            productQuantity = 1,
            item_id = _productData.value!!.productId
        )
        //log event
        val productBundle = Bundle().apply {
            putString("name", _productData.value!!.productName)
        }
        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(productBundle))
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(FirebaseAnalytics.Param.VALUE, _productData.value!!.productPrice.toDouble())
        }

        viewModelScope.launch {
            try {
                cartRepository.insertProductData(newEntity)
                triggerSnackbar("Item added to cart")
            } catch (e: Throwable) {
                triggerSnackbar(e.message!!)
            }

        }
    }

    fun getProductID(): String {
        return productRepository.selectedProductID!!
    }

    fun parcelizeProduct(): CheckoutList {
        val data = _productData.value!!
        val newCheckoutData = CheckoutData(
            productPrice = data.productPrice,
            productImage = data.image[0],
            productQuantity = 1,
            productName = data.productName,
            productStock = data.stock,
            productVariant = data.productVariant[selectedVariantIndex].variantName,
            item_id = data.productId
        )
        return CheckoutList(listOf(newCheckoutData))
    }

}