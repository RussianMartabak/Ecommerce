package com.martabak.ecommerce.status

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatusParcel  (
    val invoiceId : String,
    val date : String,
    val time : String,
    val payment : String,
    val invoiceSum : Int
) : Parcelable