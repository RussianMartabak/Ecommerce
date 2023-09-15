package com.martabak.ecommerce.product_detail.compose.product_detail_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.martabak.ecommerce.R

@Composable
fun BottomButton(show : Boolean, addCart : () -> Unit, buyNow : () -> Unit) {
    if (show) {
        Column(
            modifier = Modifier
                .height(56.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Divider(Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Row() {
                OutlinedButton(
                    onClick = buyNow,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.product_buynow),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins))
                    )
                }

                Button(onClick = addCart, modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.product_addcart),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins))
                    )
                }
            }
        }
    }

}