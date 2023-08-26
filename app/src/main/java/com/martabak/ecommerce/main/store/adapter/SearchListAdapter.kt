package com.martabak.ecommerce.main.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martabak.ecommerce.databinding.SearchItemBinding

class SearchListAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<String, SearchListAdapter.SearchViewHolder>(DiffCallback) {

    class SearchViewHolder(val onClick: (String) -> Unit, private var binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mastercard.setOnClickListener {
                onClick(binding.name.text.toString())
            }
        }

        fun bind(text: String) {
            binding.name.text = text
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(onClick, binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val name = getItem(position)
        holder.bind(name)
    }
}

object DiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }


}
