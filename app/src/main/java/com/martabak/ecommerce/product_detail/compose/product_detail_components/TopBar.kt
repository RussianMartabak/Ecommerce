package com.martabak.ecommerce.product_detail.compose.product_detail_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.martabak.ecommerce.R
import okhttp3.internal.wait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigateBack : () -> Boolean) {
        Column(Modifier.background(Color.White)) {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.product_detail), fontFamily = FontFamily(Font(R.font.poppins)))
            }, navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.arrow_nav_16),
                    null,
                    modifier = Modifier
                        .clickable { navigateBack() }
                        .padding(8.dp))
            })
            Divider(Modifier.fillMaxWidth())
        }

}