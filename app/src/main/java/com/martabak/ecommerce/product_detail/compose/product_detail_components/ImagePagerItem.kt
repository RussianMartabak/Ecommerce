package com.martabak.ecommerce.product_detail.compose.product_detail_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.martabak.ecommerce.R

@Composable
fun ImagePagerItem(i : Int, imageList : List<String>) {
    AsyncImage(
        modifier = Modifier
            .height(309.dp)
            .fillMaxWidth(),
        model = imageList[i],
        contentDescription = null,
        placeholder = painterResource(
            id = R.drawable.thumbnail
        ),
        error = painterResource(id = R.drawable.thumbnail)
    )
}