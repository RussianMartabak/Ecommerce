package com.martabak.ecommerce.main.wishlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martabak.ecommerce.database.WishlistEntity
import com.martabak.ecommerce.databinding.WishlistGridItemBinding
import com.martabak.ecommerce.databinding.WishlistLinearItemBinding
import com.martabak.ecommerce.main.wishlist.WishlistViewModel

class WishlistAdapter(
    private val viewModel: WishlistViewModel, private val onClick: (String) -> Unit
) : ListAdapter<WishlistEntity, RecyclerView.ViewHolder>(WishComparator) {
    private var gridMode = false
    fun setGridMode(b: Boolean) {
        gridMode = b
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (!gridMode) {
            val binding = WishlistLinearItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return LinearWishViewHolder(binding = binding, viewModel = viewModel, onClick = onClick)
        } else {
            val binding =
                WishlistGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GridWishViewHolder(viewModel = viewModel, binding = binding, onClick = onClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is LinearWishViewHolder) {
            holder.bind(item)
        } else if (holder is GridWishViewHolder) {
            holder.bind(item)
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