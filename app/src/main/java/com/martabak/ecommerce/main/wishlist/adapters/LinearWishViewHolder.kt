package com.martabak.ecommerce.main.wishlist.adapters

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.database.WishlistEntity
import com.martabak.ecommerce.databinding.WishlistLinearItemBinding
import com.martabak.ecommerce.main.wishlist.WishlistViewModel
import java.text.NumberFormat

class LinearWishViewHolder(
    private val viewModel: WishlistViewModel,
    private val binding: WishlistLinearItemBinding,
    private val onClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wish: WishlistEntity) {
        binding.productTitle.text = wish.productName
        val formattedPrice =
            NumberFormat.getInstance().format(wish.productPrice).replace(",", ".")
        binding.productPrice.text = "Rp$formattedPrice"
        binding.productSeller.text = wish.productSeller
        val formattedRating = String.format("%.1f", wish.productRating)
        binding.productInfo.text = "$formattedRating | Terjual ${wish.productSale}"
        binding.productImage.load(wish.productImage) {
            placeholder(R.drawable.thumbnail)
            error(R.drawable.thumbnail)
        }
        binding.addCartButton.setOnClickListener {
            viewModel.insertItem(wish)
        }

        binding.deleteWishButton.setOnClickListener {
            viewModel.deleteItem(wish.item_id)
        }

        binding.rootLinearCard.setOnClickListener {
            onClick(wish.item_id)
        }
    }
}
