package com.martabak.ecommerce.product_detail.adapters

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.ReviewItemBinding
import com.martabak.ecommerce.network.data.product_detail.ReviewData

class ReviewAdapter(private val reviews: List<ReviewData>) {

    class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(data : ReviewData) {
                binding.reviewerName.text = data.userName
                binding.reviewRating.rating = data.userRating.toFloat()
                binding.commentDescription.text = data.userReview
                val imgUri = data.userImage.toUri().buildUpon().scheme("http").build()
                binding.reviewerImage.load(imgUri) {
                    placeholder(R.drawable.thumbnail)
                    error(R.drawable.thumbnail)
                }

            }
    }


}