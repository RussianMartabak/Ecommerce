package com.martabak.ecommerce.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val item_id: String,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_price") val productPrice: Int,
    @ColumnInfo(name = "image") val productImage: String,
    @ColumnInfo(name = "product_seller") val productSeller: String,
    @ColumnInfo(name = "product_rating") val productRating: Double,
    @ColumnInfo(name = "product_sale") val productSale: Int,
    @ColumnInfo(name = "product_variant") val productVariant: String,
    @ColumnInfo(name = "product_stock") val productStock: Int
)
