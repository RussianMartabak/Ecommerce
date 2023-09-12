package com.martabak.ecommerce.product_detail.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.martabak.ecommerce.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigateBack : () -> Boolean) {
        Column() {
            TopAppBar(title = {
                Text(text = "Detail Produk")
            }, navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.arrow_nav_16),
                    null,
                    modifier = Modifier
                        .clickable { navigateBack }
                        .padding(8.dp))
            })
            Divider(Modifier.fillMaxWidth())
        }

}