package com.martabak.ecommerce.main.wishlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martabak.ecommerce.database.WishlistEntity
import com.martabak.ecommerce.databinding.WishlistLinearItemBinding
import com.martabak.ecommerce.main.wishlist.WishlistViewModel

class WishlistAdapter(
    private val viewModel: WishlistViewModel,
    private val onClick: (String) -> Unit
) : ListAdapter<WishlistEntity, RecyclerView.ViewHolder>(WishComparator) {
    private var gridMode = false
    fun setGridMode(b: Boolean) {
        gridMode = b
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinearWishViewHolder {
        val binding =
            WishlistLinearItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        if (!gridMode) {

            return LinearWishViewHolder(binding = binding, viewModel = viewModel, onClick = onClick)
        }
        return LinearWishViewHolder(binding = binding, viewModel = viewModel, onClick = onClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (!gridMode) {
            (holder as LinearWishViewHolder).bind(item)
        }
    }


}

object WishComparator : DiffUtil.ItemCallback<WishlistEntity>() {
    override fun areItemsTheSame(oldItem: WishlistEntity, newItem: WishlistEntity): Boolean {
        return oldItem.item_id == newItem.item_id
    }

    override fun areContentsTheSame(oldItem: WishlistEntity, newItem: WishlistEntity): Boolean {
        return oldItem == newItem
    }

}