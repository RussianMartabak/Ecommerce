package com.martabak.ecommerce.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martabak.ecommerce.network.data.checkout.CheckoutData


class CheckoutViewModel : ViewModel() {
    private var _itemList = MutableLiveData<List<CheckoutData>>()
    val itemList : LiveData<List<CheckoutData>> = _itemList


    fun submitItemList(data : List<CheckoutData>) {
        _itemList.value = data
    }

    //given an id, add count to that item
    fun addItemCount(id : String) {
        //get the object
        val target = _itemList.value!!.first{it.item_id == id}
        val targetIndex = _itemList.value!!.indexOf(target)
        val listCopy : List<CheckoutData> = deepCopyList(_itemList.value!!)
        if (listCopy[targetIndex].productStock != 0) {
            listCopy[targetIndex].productQuantity += 1
            listCopy[targetIndex].productStock -= 1
            _itemList.value = listCopy
        }

    }

    fun decreaseItemCount(id : String) {
        val target = _itemList.value!!.first{it.item_id == id}
        val targetIndex = _itemList.value!!.indexOf(target)
        val listCopy : List<CheckoutData> = deepCopyList(_itemList.value!!)
        if (listCopy[targetIndex].productQuantity > 1) {
            listCopy[targetIndex].productStock += 1
            listCopy[targetIndex].productQuantity -= 1
            _itemList.value = listCopy
        }
    }

    private fun deepCopyList(list : List<CheckoutData>) : List<CheckoutData> {
        val newList = mutableListOf<CheckoutData>()
        list.forEach { it ->
            newList.add(it.copy())
        }
        return newList
    }


}