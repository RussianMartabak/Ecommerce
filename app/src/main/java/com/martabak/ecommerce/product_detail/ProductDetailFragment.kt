package com.martabak.ecommerce.product_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.martabak.core.network.data.product_detail.Data
import com.martabak.core.network.data.product_detail.ProductVariant
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentProductDetailBinding
import com.martabak.ecommerce.product_detail.adapters.ProductDetailAdapter
import com.martabak.ecommerce.utils.GlobalUtils.logButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

private const val ARG_PRODUCT_ID = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailViewModel by viewModels()
    private var connectionOK = false
    private var productData: Data? = null
    private var variants: List<ProductVariant>? = null
    private val args: ProductDetailFragmentArgs by navArgs()

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.productID?.let {
            viewModel.setProductID(it)
        }
        viewModel.getProductData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingIndicator.isVisible = false
        binding.errorLayout.isVisible = false
        binding.normalLayout.isVisible = false

        binding.allReviewButton.setOnClickListener {

            findNavController().navigate(R.id.action_productDetailFragment_to_reviewFragment)
        }

        binding.imageIndicator.isVisible = false

        binding.topBarDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.refreshButton.setOnClickListener {
            analytics.logButton("refresh")
            viewModel.getProductData()
        }

        viewModel.connectionSuccess.observe(viewLifecycleOwner) {
            if (it) {
                connectionOK = it
                binding.normalLayout.isVisible = true
                binding.errorLayout.isVisible = false
            } else {
                binding.normalLayout.isVisible = false
                binding.errorLayout.isVisible = true
            }
        }

        viewModel.nowLoading.observe(viewLifecycleOwner) {
            binding.loadingIndicator.isVisible = it
        }

        binding.variantChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.size > 0) {
                val selectedChip = group.findViewById<Chip>(checkedIds[0])
                val variantName = selectedChip.text.toString()
                val variantObject = variants!!.first { it.variantName == variantName }
                viewModel.updateProductPrice(variantObject.variantPrice)
                viewModel.selectedVariantIndex = variants!!.indexOf(variantObject)
                // get the price!!!
            }
        }

        binding.favButton.setOnClickListener {
            analytics.logButton("wishlist")
            viewModel.processWishlist()
        }

        viewModel.productOnWishlist.observe(viewLifecycleOwner) {
            if (it) {
                binding.favButton.setImageResource(R.drawable.fav_24)
            } else {
                binding.favButton.setImageResource(R.drawable.fav_border_24)
            }
        }

        binding.shareButton.setOnClickListener {
            analytics.logButton("share")
            val id = viewModel.getProductID()
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "http://scheissekomputer/$id")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        viewModel.eventLivedata.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }

        }

        binding.addCartButton.setOnClickListener {
            analytics.logButton("add to cart")
            viewModel.addToCart()
        }

        viewModel.currentPrice.observe(viewLifecycleOwner) {
            binding.productPrice.text = integerToRupiah(it)
        }

        binding.directBuyButton.setOnClickListener {
            analytics.logButton("buy now")
            val paket = viewModel.parcelizeProduct()
            val aktion = ProductDetailFragmentDirections.startCheckoutFromProduct(paket)
            findNavController().navigate(aktion)
        }

        viewModel.productData.observe(viewLifecycleOwner) {
            displayData(it)
        }
    }

    private fun displayData(it: Data) {
        productData = it
        // set displays
        val priceSum = it.productPrice + it.productVariant[0].variantPrice
        binding.productPrice.text = integerToRupiah(priceSum)
        binding.productTitle.text = it.productName
        binding.productSale.text = "Terjual ${it.sale}"
        binding.upperReviewText.text = "${doubleToRating(it.productRating)} (${it.totalRating})"
        binding.productDescription.text = it.description
        binding.reviewBigText.text = "${doubleToRating(it.productRating)}"
        binding.productPopularity.text = "${it.totalSatisfaction}% pembeli merasa puas"
        binding.ratingOverview.text = "${it.totalRating} rating· ${it.totalReview} ulasan"
        variants = it.productVariant
        makeChips()
        val imageList = it.image
        val pagerAdapter = ProductDetailAdapter(imageList)
        binding.imagePager.adapter = pagerAdapter
        if (imageList.size > 1) {
            binding.imageIndicator.isVisible = true
            TabLayoutMediator(
                binding.imageIndicator,
                binding.imagePager
            ) { _, _ -> }.attach()
        }
        binding.normalLayout.isVisible = true
    }

    fun integerToRupiah(value: Int): String {
        var price = NumberFormat.getInstance().format(value).replace(",", ".")
        return "Rp$price"
    }

    fun doubleToRating(value: Double): String {
        return String.format("%.1f", value)
    }

    fun makeChips() {
        var chipIndex = 0
        variants!!.forEach {
            val newChip = Chip(
                requireActivity(),
                null,
                com.google.android.material.R.attr.chipStandaloneStyle
            )
            if (chipIndex == 0) {
                newChip.isChecked = true
            }
            newChip.isCheckedIconVisible = false
            val title = it.variantName
            newChip.text = title
            binding.variantChipGroup.addView(newChip)
            chipIndex += 1
        }
    }
}
