package com.martabak.ecommerce.product_detail.compose.product_detail_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun BigStarBox(productDetail : Data) {
    Row(modifier = Modifier.padding(end = 32.dp), verticalAlignment = Alignment.Top) {
        Image(
            painter = painterResource(id = R.drawable.star),
            modifier = Modifier.padding(top = 2.dp).size(24.dp),
            contentDescription = null
        )

        Spacer(Modifier.width(4.dp))
        Text(
            text = doubleToRating(productDetail.productRating),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.poppins_semibold))
        )
        Text(
            text = "/5.0",
            fontSize = 10.sp,
            fontFamily = FontFamily(Font(R.font.poppins)),
            modifier = Modifier.padding(top = 10.dp)
        )

    }
}