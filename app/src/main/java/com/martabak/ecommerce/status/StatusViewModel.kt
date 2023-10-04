package com.martabak.ecommerce.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martabak.core.network.ApiService
import com.martabak.core.network.data.rating.RatingBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {
    var rating: Int? = null
    var review: String? = null
    var parcel: StatusParcel? = null

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    // send le package to france
    fun sendRating() {
        val body = RatingBody(invoiceId = parcel!!.invoiceId, rating = rating, review = review)
        viewModelScope.launch {
            try {
                val response = apiService.postRating(body)
                _success.value = true
            } catch (e: Throwable) {
            }
        }
    }
}
