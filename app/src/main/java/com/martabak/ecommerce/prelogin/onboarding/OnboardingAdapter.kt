package com.martabak.ecommerce.prelogin.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.martabak.ecommerce.R

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {
    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val pictures =
        listOf<Int>(R.drawable.onboard_1, R.drawable.onboard_2, R.drawable.onboard_3)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.heroImage)
        imageView.setImageResource(pictures[position])
    }
}
