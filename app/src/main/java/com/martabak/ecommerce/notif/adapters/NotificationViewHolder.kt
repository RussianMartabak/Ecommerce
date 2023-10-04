package com.martabak.ecommerce.notif.adapters

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.color.MaterialColors
import com.martabak.ecommerce.R
import com.martabak.core.database.NotifEntity
import com.martabak.ecommerce.databinding.NotificationItemBinding
import com.martabak.ecommerce.notif.NotificationViewModel

class NotificationViewHolder(
    private val binding: NotificationItemBinding,
    private val viewModel: NotificationViewModel
) :
    RecyclerView.ViewHolder(binding.root) {
    var itemId: Int = 0

    @SuppressLint("ResourceAsColor")
    fun bind(itemData: NotifEntity) {
        Log.d("zaky", "VH binding executed")
        itemId = itemData.notif_id
        Log.d("zaky", "Notif id $itemId read value : ${itemData.read}")
        binding.type.text = itemData.type
        val background = if (itemData.read) {
            com.google.android.material.R.attr.colorSurfaceContainerLowest
        } else {
            com.google.android.material.R.attr.colorPrimaryContainer
        }
        val backgroundColor = MaterialColors.getColor(itemView, background)
        itemView.setBackgroundColor(backgroundColor)
        binding.datum.text = itemData.datetime
        binding.image.load(itemData.image) {
            error(R.drawable.thumbnail)
            placeholder(R.drawable.thumbnail)
        }
        binding.notifTitle.text = itemData.title
        binding.body.text = itemData.body

        binding.notifRoot.setOnClickListener {
            Log.d("zaky", "itemId: $itemId is clicked")
            viewModel.updateNotif(itemId, true)
        }
    }
}
