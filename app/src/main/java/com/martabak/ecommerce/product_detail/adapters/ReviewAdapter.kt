package com.martabak.ecommerce.product_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.core.network.data.product_detail.ReviewData
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.ReviewItemBinding

class ReviewAdapter(private val reviews: List<ReviewData>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ReviewData) {
            binding.reviewerName.text = data.userName
            binding.reviewRating.rating = data.userRating.toFloat()
            binding.commentDescription.text = data.userReview
            binding.reviewerImage.load(data.userImage) {
                placeholder(R.drawable.thumbnail)
                error(R.drawable.thumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = reviews[position]
        holder.bind(item)
    }
}
