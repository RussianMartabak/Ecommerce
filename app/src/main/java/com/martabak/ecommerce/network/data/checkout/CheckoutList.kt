package com.martabak.ecommerce.network.data.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutList(
    val itemList: List<CheckoutData> = emptyList()
) : Parcelable
