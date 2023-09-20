package com.martabak.ecommerce.notif.adapters

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.database.NotifEntity
import com.martabak.ecommerce.databinding.NotificationItemBinding

class NotificationViewHolder(private val binding: NotificationItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(itemData : NotifEntity) {
            binding.type.text = itemData.type
            binding.datum.text = itemData.datetime
            binding.image.load(itemData.image) {
                error(R.drawable.thumbnail)
                placeholder(R.drawable.thumbnail)
            }
            binding.notifTitle.text = itemData.title
            binding.body.text = itemData.body
        }
}