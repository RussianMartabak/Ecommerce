package com.martabak.core.network.data.checkout

import android.os.Parcelable
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutList(
    val itemList: List<CheckoutData> = emptyList()
) : Parcelable
