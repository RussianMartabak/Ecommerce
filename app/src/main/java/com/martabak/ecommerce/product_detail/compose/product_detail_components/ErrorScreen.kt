package com.martabak.ecommerce.product_detail.compose.product_detail_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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

@Composable
fun ErrorScreen(refreshFunction : () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 150.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.store_error_smartphone),
            null, modifier = Modifier.size(128.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Empty",
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.poppins_medium))
        )
        Text(
            text = "Your requested data is unavailable",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.poppins))
        )
        Button(onClick = refreshFunction) {
            Text(text = "Refresh", fontFamily = FontFamily(Font(R.font.poppins)))
        }
    }
}