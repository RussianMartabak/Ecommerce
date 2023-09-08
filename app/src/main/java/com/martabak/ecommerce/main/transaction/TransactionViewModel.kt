package com.martabak.ecommerce.main.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.transaction.TransactionData
import com.martabak.ecommerce.status.StatusParcel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {
    //hit da API
    private val _transactionList = MutableLiveData<List<TransactionData>>()
    val transactionList: LiveData<List<TransactionData>> = _transactionList

    //given an id, return a statusparcel object
    fun getStatusParcel(id: String): StatusParcel {
        val transData: TransactionData = _transactionList.value!!.first { it.invoiceId == id }
        return StatusParcel(
            invoiceId = transData.invoiceId,
            date = transData.date,
            time = transData.time,
            payment = transData.payment,
            invoiceSum = transData.total
        )

    }

    fun getTransactions() {
        viewModelScope.launch {
            try {
                val response = apiService.getTransactions()
                _transactionList.value = response.data
            } catch (e: Throwable) {

            }
        }
    }
}