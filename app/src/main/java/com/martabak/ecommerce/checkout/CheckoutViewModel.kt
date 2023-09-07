package com.martabak.ecommerce.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentBody
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {
    private var _itemList = MutableLiveData<List<CheckoutData>>()
    val itemList: LiveData<List<CheckoutData>> = _itemList

    private var _payment = MutableLiveData<String>()
    val payment: LiveData<String> = _payment

    private var _nowLoading = MutableLiveData<Boolean>()
    val nowLoading: LiveData<Boolean> = _nowLoading

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    //ask for fulfillment response then parcel and send it to status
    fun sendFulfillment() {
        viewModelScope.launch {
            //make body
            val body = convertListToFullfillment(_payment.value!!, _itemList.value!!)
            try {
                _nowLoading.value = true
                val response = apiService.sendForFulfillment(body)
                _nowLoading.value = false

            } catch (e : Throwable) {
                _errorMessage.value = e.toString()
                _nowLoading.value = false
            }
        }

    }

    fun setPayment(s: String) {
        _payment.value = s
    }


    fun submitItemList(data: List<CheckoutData>) {
        _itemList.value = data
    }

    //given an id, add count to that item
    fun addItemCount(id: String) {
        //get the object
        val target = _itemList.value!!.first { it.item_id == id }
        val targetIndex = _itemList.value!!.indexOf(target)
        val listCopy: List<CheckoutData> = deepCopyList(_itemList.value!!)
        if (listCopy[targetIndex].productStock != 0) {
            listCopy[targetIndex].productQuantity += 1
            listCopy[targetIndex].productStock -= 1
            _itemList.value = listCopy
        }

    }

    fun decreaseItemCount(id: String) {
        val target = _itemList.value!!.first { it.item_id == id }
        val targetIndex = _itemList.value!!.indexOf(target)
        val listCopy: List<CheckoutData> = deepCopyList(_itemList.value!!)
        if (listCopy[targetIndex].productQuantity > 1) {
            listCopy[targetIndex].productStock += 1
            listCopy[targetIndex].productQuantity -= 1
            _itemList.value = listCopy
        }
    }

    private fun deepCopyList(list: List<CheckoutData>): List<CheckoutData> {
        val newList = mutableListOf<CheckoutData>()
        list.forEach { it ->
            newList.add(it.copy())
        }
        return newList
    }

    private fun convertListToFullfillment(payment : String, items : List<CheckoutData>) : FulfillmentBody {
        val neueList = items.map {
            convertToFulfillmentItem(it)
        }
        return FulfillmentBody(payment = payment, items = neueList)
    }

    private fun convertToFulfillmentItem(obj: CheckoutData): FulfillmentItem {
        return FulfillmentItem(
            productId = obj.item_id,
            quantity = obj.productQuantity,
            variantName = obj.productVariant
        )
    }
}