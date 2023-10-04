package com.martabak.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val item_id: String,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_variant") val productVariant: String,
    @ColumnInfo(name = "product_stock") val productStock: Int,
    @ColumnInfo(name = "product_price") val productPrice: Int,
    @ColumnInfo(name = "product_qty") val productQuantity: Int,
    @ColumnInfo(name = "selected") val isSelected: Boolean,
    @ColumnInfo(name = "image") val productImage: String
)
