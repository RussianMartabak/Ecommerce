package com.martabak.ecommerce.network.data.fulfillment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class FulfillmentResponse(
    val code: Int,
    val message: String,
    val `data`: FulfillmentData
)

@Parcelize
data class FulfillmentData(
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int
) : Parcelable
