package com.martabak.ecommerce.main.wishlist.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.martabak.ecommerce.R
import com.martabak.core.database.WishlistEntity
import com.martabak.ecommerce.databinding.WishlistGridItemBinding
import com.martabak.ecommerce.main.wishlist.WishlistViewModel
import java.text.NumberFormat

class GridWishViewHolder(
    private val viewModel: WishlistViewModel,
    private val binding: WishlistGridItemBinding,
    private val onClick: (String) -> Unit,
    val context : Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wish: WishlistEntity) {
        binding.productTitleGrid.text = wish.productName
        val formattedPrice =
            NumberFormat.getInstance().format(wish.productPrice).replace(",", ".")
        binding.productPriceGrid.text = "Rp$formattedPrice"
        binding.productSellerGrid.text = wish.productSeller
        val formattedRating = String.format("%.1f", wish.productRating)
        val soldString = context.resources.getString(R.string.sold)
        binding.productInfoGrid.text = "$formattedRating | $soldString ${wish.productSale}"
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
