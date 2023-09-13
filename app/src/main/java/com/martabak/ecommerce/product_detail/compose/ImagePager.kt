package com.martabak.ecommerce.product_detail.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagePager(imageList : List<String>) {
    val pagerState = rememberPagerState()
    Box() {
        HorizontalPager(pageCount = imageList.size, state = pagerState) {
            ImagePagerItem(i = it, imageList = imageList)
        }
        Row(
            Modifier
                .height(30.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(imageList.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFF6750A4) else Color(0xFFCAC4D0)
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}