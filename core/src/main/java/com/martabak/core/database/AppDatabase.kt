package com.martabak.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartEntity::class, WishlistEntity::class, NotifEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun notifDao(): NotifDao
}
