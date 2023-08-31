package com.martabak.ecommerce.product_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.GlobalState
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(val productRepository: ProductRepository, val globalState : GlobalState) :
    ViewModel() {
        private var _currentPrice = MutableLiveData<Int>()
        var currentPrice : LiveData<Int> = _currentPrice


        private var basePrice : Int = 0

        fun updateProductPrice(variantPrice : Int) {
            _productData.value!!.productPrice = basePrice + variantPrice
            _currentPrice.value = basePrice + variantPrice
        }

        fun setProductViewing() {
            globalState.inProductDetail = true
        }

        private var _connectionSuccess : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        var connectionSuccess : LiveData<Boolean> = _connectionSuccess

        private var _productData : MutableLiveData<Data> = MutableLiveData<Data>()
        var productData : LiveData<Data> = _productData


        var errorMessage = ""

        fun getProductData(){
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
        fun getProductID() : String {
            return productRepository.selectedProductID!!
        }

}