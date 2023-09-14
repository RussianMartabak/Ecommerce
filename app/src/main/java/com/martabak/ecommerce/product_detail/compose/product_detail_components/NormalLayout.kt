package com.martabak.ecommerce.product_detail.compose.product_detail_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.martabak.ecommerce.R
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.network.data.product_detail.ProductVariant
import java.text.NumberFormat

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NormalLayout(
    productDetail: Data?,
    onWishlist: Boolean?,
    processWish: () -> Unit,
    shareLink: () -> Unit,
    updatePrice: (ProductVariant) -> Unit,
    updatedPrice: Int?,
    toReview : () -> Unit,

) {
    val imageList = productDetail!!.image
    ImagePager(imageList = imageList)
    Spacer(Modifier.height(12.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = integerToRupiah(updatedPrice ?: productDetail.productPrice),
            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.share_24),
            null,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { shareLink() }
        )
        //passed boolean and a function for updating wishlist
        WishlistButton(onWishlist, processWish)

    }
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = productDetail.productName,
        fontFamily = FontFamily(Font(R.font.poppins)),
        fontSize = 14.sp,
    )
    Spacer(Modifier.height(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Terjual ${productDetail.sale}",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.poppins))
        )
        Spacer(modifier = Modifier.width(8.dp))
        ReviewBox(productDetail)

    }
    Spacer(Modifier.height(12.dp))
    Divider(Modifier.fillMaxWidth())
    Spacer(Modifier.height(12.dp))
    Text(
        text = "Pilih Varian",
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    FlowRow(Modifier.padding(horizontal = 16.dp, vertical = 0.dp)) {
        val variantList = productDetail.productVariant
        val selectedVariant = remember { mutableStateOf(variantList[0]) }
        val updateChipState = { variant: ProductVariant ->
            updatePrice(variant)
            selectedVariant.value = variant
        }
        FilterChipGroup(
            selectedVariant = selectedVariant.value,
            updatePrice = updateChipState,
            variantList = variantList
        )
    }
    Spacer(Modifier.height(4.dp))
    Divider(Modifier.fillMaxWidth())
    Spacer(Modifier.height(12.dp))
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = "Deskripsi Produk",
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.poppins_medium))
    )
    Text(
        text = productDetail.description,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.poppins))
    )
    Spacer(Modifier.height(12.dp))
    Divider(Modifier.fillMaxWidth())
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Ulasan Pembeli",
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        TextButton(onClick = toReview, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Lihat Semua")
        }
    }
    Row(
        modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 18.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        BigStarBox(productDetail = productDetail)
        Column {
            Text(
                text = "${productDetail.totalSatisfaction}% pembeli merasa puas",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
            Text(
                text = "${productDetail.totalRating} rating · ${productDetail.totalReview} ulasan",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.poppins))
            )

        }
    }

}

fun integerToRupiah(value: Int): String {
    val price = NumberFormat.getInstance().format(value).replace(",", ".")
    return "Rp$price"
}

