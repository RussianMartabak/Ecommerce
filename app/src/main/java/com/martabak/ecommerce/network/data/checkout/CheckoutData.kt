package com.martabak.ecommerce.network.data.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutData(
    val productImage: String,
    val productName: String,
    var productStock: Int,
    val productPrice: Int,
    val productVariant: String,
    var productQuantity: Int,
    val item_id: String
) : Parcelable
