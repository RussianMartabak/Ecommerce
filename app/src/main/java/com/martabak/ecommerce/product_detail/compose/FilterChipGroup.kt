package com.martabak.ecommerce.product_detail.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.martabak.ecommerce.network.data.product_detail.ProductVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipGroup(
    selectedVariant: ProductVariant,
    updatePrice: (ProductVariant) -> Unit,
    variantList: List<ProductVariant>
) {
    variantList.forEach { variant ->
        //make le function to send price to viewmodel
        FilterChip(
            selected = selectedVariant == variant,
            onClick = {
                updatePrice(variant)
                      },
            label = { Text(text = variant.variantName) },
            modifier = Modifier.padding(end = 8.dp)
        )

    }
}