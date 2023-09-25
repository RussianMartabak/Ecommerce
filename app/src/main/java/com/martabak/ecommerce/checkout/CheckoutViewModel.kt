package com.martabak.ecommerce.checkout

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentBody
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentData
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentItem
import com.martabak.ecommerce.network.data.fulfillment.FulfillmentResponse
import com.martabak.ecommerce.status.StatusParcel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val apiService: ApiService, private val analytics: FirebaseAnalytics) : ViewModel() {
    private var _itemList = MutableLiveData<List<CheckoutData>>()
    val itemList: LiveData<List<CheckoutData>> = _itemList

    var sum : Long = 0

    private var _payment = MutableLiveData<String>()
    val payment: LiveData<String> = _payment

    private var _nowLoading = MutableLiveData<Boolean>()
    val nowLoading: LiveData<Boolean> = _nowLoading

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var _statusParcel = MutableLiveData<StatusParcel>()
    val statusParcel : LiveData<StatusParcel> = _statusParcel

    //ask for fulfillment response then parcel and send it to status
    fun sendFulfillment() {
        viewModelScope.launch {
            //make body
            val body = convertListToFullfillment(_payment.value!!, _itemList.value!!)
            try {
                _nowLoading.value = true
                val response = apiService.sendForFulfillment(body)
                logPurchase(response)
                _statusParcel.value = convertToStatusParcel(response.data)
                _nowLoading.value = false

            } catch (e: Throwable) {
                _errorMessage.value = e.toString()
                _nowLoading.value = false
            }
        }

    }

    fun setPayment(s: String) {
        _payment.value = s
    }


    private fun logPurchase(r : FulfillmentResponse) {
        var sum : Long= 0
        val itemList = mutableListOf<Bundle>()
        _itemList.value!!.forEach {
            itemList.add(bundleOf("name" to it.productName))
            sum += it.productPrice
        }


        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE) {
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(FirebaseAnalytics.Param.ITEMS, itemList.toTypedArray())
            param(FirebaseAnalytics.Param.TRANSACTION_ID, r.data.invoiceId)
            param(FirebaseAnalytics.Param.VALUE, sum)
        }
    }

    fun submitItemList(data: List<CheckoutData>) {
        _itemList.value = data
        sum = 0
        //log event

        val checkoutList = mutableListOf<Bundle>()
        data.forEach { item ->
            checkoutList.add(bundleOf("name" to item.productName))
            sum += item.productPrice
        }
        val checkoutArray = checkoutList.toTypedArray()
        analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(FirebaseAnalytics.Param.ITEMS, checkoutArray)
            param(FirebaseAnalytics.Param.VALUE, sum)
        }
    }

    //given an id, add count to that item
    fun addItemCount(id: String) {
        //get the object
        val target = _itemList.value!!.first { it.item_id == id }
        val targetIndex = _itemList.value!!.indexOf(target)
        val listCopy: List<CheckoutData> = deepCopyList(_itemList.value!!)
        //precondition for adding item
        if (listCopy[targetIndex].productStock > listCopy[targetIndex].productQuantity) {
            listCopy[targetIndex].productQuantity += 1
            _itemList.value = listCopy
        }

    }

    fun decreaseItemCount(id: String) {
        val target = _itemList.value!!.first { it.item_id == id }
        val targetIndex = _itemList.value!!.indexOf(target)
        val listCopy: List<CheckoutData> = deepCopyList(_itemList.value!!)
        if (listCopy[targetIndex].productQuantity > 1) {
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

    private fun convertListToFullfillment(
        payment: String,
        items: List<CheckoutData>
    ): FulfillmentBody {
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

    private fun convertToStatusParcel(response: FulfillmentData): StatusParcel {
        return StatusParcel(
            invoiceId = response.invoiceId,
            date = response.date,
            invoiceSum = response.total,
            payment = response.payment,
            time = response.time
        )
    }
}