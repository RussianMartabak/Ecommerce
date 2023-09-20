package com.martabak.ecommerce.notif.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.martabak.ecommerce.database.NotifEntity
import com.martabak.ecommerce.databinding.NotificationItemBinding

class NotifAdapter : ListAdapter<NotifEntity, NotificationViewHolder>(NotifComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}