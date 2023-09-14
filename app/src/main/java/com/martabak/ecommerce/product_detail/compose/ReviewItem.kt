package com.martabak.ecommerce.product_detail.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.martabak.ecommerce.R
import com.martabak.ecommerce.network.data.product_detail.ReviewData

@Composable
fun ReviewItem(data: ReviewData) {
    Column() {
        Divider(Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Row(Modifier.padding(horizontal = 16.dp)) {
            AsyncImage(
                model = data.userImage,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(
                        CircleShape
                    ),
                error = painterResource(id = R.drawable.thumbnail),
                placeholder = painterResource(id = R.drawable.thumbnail)
            )
        }
    }
}