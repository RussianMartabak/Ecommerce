package com.martabak.ecommerce.product_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.GlobalState
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.repository.CartRepository
import com.martabak.ecommerce.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    val productRepository: ProductRepository,
    val globalState: GlobalState,
    val cartRepository: CartRepository
) :
    ViewModel() {
    private var _currentPrice = MutableLiveData<Int>()
    var currentPrice: LiveData<Int> = _currentPrice

    var selectedVariantIndex = 0

    private var basePrice: Int = 0

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


    var errorMessage = ""

    fun getProductData() {
        viewModelScope.launch {
            try {
                val response = productRepository.getProductDetail()
                Log.d("zaky", "response in viewmodel $response")
                _connectionSuccess.value = true
                _productData.value = response
                basePrice = response.productPrice
            } catch (e: Throwable) {
                Log.d("zaky", "product detail error $e")
                _connectionSuccess.value = false

            }
        }
    }
    fun triggerSnackbar() {
        viewModelScope.launch {
            eventChannel.send("Item added to cart")
        }
    }
    fun addToCart() {
        val newEntity = CartEntity(
            productName = _productData.value!!.productName,
            productVariant = _productData.value!!.productVariant[0].variantName,
            productStock = _productData.value!!.stock,
            isSelected = false,
            productImage = _productData.value!!.image[0],
            productPrice = basePrice,
            productQuantity = 1,
            item_id = _productData.value!!.productId
        )
        viewModelScope.launch {
            try {
                cartRepository.insertProductData(newEntity)
                triggerSnackbar()
            } catch(e: Throwable){
                Log.d("zaky", "$e")
            }

        }
    }

    fun getProductID(): String {
        return productRepository.selectedProductID!!
    }

}