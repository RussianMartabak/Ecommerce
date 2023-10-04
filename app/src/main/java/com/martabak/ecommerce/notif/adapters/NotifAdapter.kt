package com.martabak.ecommerce.notif.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.martabak.core.database.NotifEntity
import com.martabak.ecommerce.databinding.NotificationItemBinding
import com.martabak.ecommerce.notif.NotificationViewModel

class NotifAdapter(private val viewModel: NotificationViewModel) : ListAdapter<NotifEntity, NotificationViewHolder>(
    NotifComparator
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
