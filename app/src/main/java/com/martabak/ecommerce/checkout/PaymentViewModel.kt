package com.martabak.ecommerce.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.martabak.ecommerce.GlobalState
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.payment.PaymentData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val apiService: ApiService,
    val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _paymentData = MutableLiveData<List<PaymentData>>()
    val paymentData: LiveData<List<PaymentData>> = _paymentData

    init {

    }

    fun getPaymentData() {
        viewModelScope.launch {
            try {
                val response = apiService.getPaymentMethods()
                _paymentData.value = response.data
            } catch (e: Throwable) {
                Log.d("zaky", "payment API error $e")
            }
        }
    }
}