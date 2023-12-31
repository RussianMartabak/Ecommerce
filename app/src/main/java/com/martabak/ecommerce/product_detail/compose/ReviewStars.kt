package com.martabak.ecommerce.product_detail.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.martabak.ecommerce.R

@Composable
fun ReviewStars(rating: Int) {
    Row {
        var index = 1
        var inactiveColor = Color.LightGray
        var activeColor = Color.Black
        if (isSystemInDarkTheme()) {
            inactiveColor = Color.DarkGray
            activeColor = Color.LightGray
        }
        repeat(5) {
            val color = if (index > rating) inactiveColor else activeColor
            Icon(painterResource(id = R.drawable.star), null, Modifier.size(16.dp), color)
            index += 1
        }
    }
}
