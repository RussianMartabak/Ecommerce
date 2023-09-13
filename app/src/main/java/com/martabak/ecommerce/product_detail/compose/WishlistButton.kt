package com.martabak.ecommerce.product_detail.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.martabak.ecommerce.R

@Composable
fun WishlistButton(onWishlist : Boolean?, processWish : () -> Unit) {
    Box(Modifier.clickable { processWish() }) {
        if (onWishlist!!) {
            Image(painter = painterResource(id = R.drawable.fav_24), null)
        } else {
            Image(painter = painterResource(id = R.drawable.fav_border_24), null)
        }
    }
}