package com.martabak.ecommerce.product_detail.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.martabak.core.network.data.product_detail.ReviewData
import com.martabak.ecommerce.R

@Composable
fun ReviewItem(data: ReviewData) {
    Column {
        Divider(Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = data.userName,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                )
                ReviewStars(rating = data.userRating)
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    Text(
        text = data.userReview,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.poppins))
    )
    Spacer(Modifier.height(16.dp))
    Divider(Modifier.fillMaxWidth())
}
