package com.martabak.ecommerce.product_detail.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.martabak.ecommerce.R
import com.martabak.ecommerce.network.data.product_detail.Data

@Composable
fun ReviewBox(productDetail: Data) {
    Box(
        Modifier
            .height(21.dp)
            .width(70.dp)
    ) {
        Image(painter = painterResource(id = R.drawable.review_box), null)
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "${doubleToRating(productDetail.productRating)} (${productDetail.totalRating})",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.poppins))
            )
        }
    }
}

fun doubleToRating(value: Double): String {
    return String.format("%.1f", value)
}