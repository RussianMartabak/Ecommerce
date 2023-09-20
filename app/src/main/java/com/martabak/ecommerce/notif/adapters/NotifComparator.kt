package com.martabak.ecommerce.notif.adapters

import androidx.recyclerview.widget.DiffUtil
import com.martabak.ecommerce.database.NotifEntity

object NotifComparator : DiffUtil.ItemCallback<NotifEntity>() {
    override fun areItemsTheSame(oldItem: NotifEntity, newItem: NotifEntity): Boolean {
        return oldItem.notif_id == newItem.notif_id
    }

    override fun areContentsTheSame(oldItem: NotifEntity, newItem: NotifEntity): Boolean {
        return oldItem == newItem
    }
}