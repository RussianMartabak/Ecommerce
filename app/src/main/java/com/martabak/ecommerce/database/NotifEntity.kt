package com.martabak.ecommerce.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notif")
data class NotifEntity(
    @PrimaryKey(autoGenerate = true)
    val notif_id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "body") val body: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "datetime") val datetime: String,
    @ColumnInfo(name = "is_read") val read: Boolean = false

)
