package com.martabak.ecommerce.main.wishlist.adapters

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.ecommerce.database.WishlistEntity
import com.martabak.ecommerce.databinding.WishlistGridItemBinding
import com.martabak.ecommerce.main.wishlist.WishlistViewModel
import java.text.NumberFormat

class GridWishViewHolder(
    private val viewModel: WishlistViewModel,
    private val binding: WishlistGridItemBinding,
    private val onClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wish: WishlistEntity) {
        binding.productTitleGrid.text = wish.productName
        val formattedPrice =
            NumberFormat.getInstance().format(wish.productPrice).replace(",", ".")
        binding.productPriceGrid.text = "Rp$formattedPrice"
        binding.productSellerGrid.text = wish.productSeller
        val formattedRating = String.format("%.1f", wish.productRating)
        binding.productInfoGrid.text = "$formattedRating | Terjual ${wish.productSale}"
        binding.productImageGrid.load(wish.productImage) {
            placeholder(R.drawable.thumbnail)
            error(R.drawable.thumbnail)
        }
        binding.addCartButtonGrid.setOnClickListener {
            viewModel.insertItem(wish)
        }

        binding.deleteWishButtonGrid.setOnClickListener {
            viewModel.deleteItem(wish.item_id)
        }

        binding.rootGridCard.setOnClickListener {
            onClick(wish.item_id)
        }
    }
}
