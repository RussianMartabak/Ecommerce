package com.martabak.ecommerce.product_detail.compose

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.martabak.ecommerce.R
import com.martabak.ecommerce.checkout.CheckoutFragmentArgs
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.network.data.product_detail.ProductVariant
import com.martabak.ecommerce.product_detail.ProductDetailViewModel
import com.martabak.ecommerce.product_detail.compose.product_detail_components.BottomButton
import com.martabak.ecommerce.product_detail.compose.product_detail_components.ErrorScreen
import com.martabak.ecommerce.product_detail.compose.product_detail_components.NormalLayout
import com.martabak.ecommerce.product_detail.compose.product_detail_components.TopBar
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailComposeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProductDetailComposeFragment : Fragment() {
    private val viewModel: ProductDetailViewModel by viewModels()
    private var productData: Data? = null
    private var variants: List<ProductVariant>? = null
    private var imageList: List<String> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProductData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                DetailScreen(viewModel = viewModel) {
                    buyNow()
                }
            }
        }
    }

    //Composable used to pass datas from viewmodel for use in production
    @Composable
    fun DetailScreen(viewModel: ProductDetailViewModel, buyNow: () -> Unit) {
        val navBack = { findNavController().navigateUp() }
        val productData by viewModel.productData.observeAsState()
        val processWish = { viewModel.processWishlist() }
        val productOnWishlist by viewModel.productOnWishlist.observeAsState()
        val snackbarMessage by viewModel.eventFlow.collectAsState(null)
        val shareLink = {
            val id = viewModel.getProductID()
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "http://scheissekomputer/$id")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        val updatedPrice by viewModel.currentPrice.observeAsState()
        val sendPrice =
            { variant: ProductVariant -> viewModel.updateProductPrice(variant.variantPrice) }
        val toReview = {
            findNavController().navigate(R.id.action_productDetailComposeFragment_to_reviewComposeFragment)
        }
        val nowLoading by viewModel.nowLoading.observeAsState(true)
        val refresh = {
            viewModel.getProductData()
        }
        val addCart = {
            viewModel.addToCart()
        }
        DetailScreen(
            navBack,
            productData,
            processWish,
            productOnWishlist,
            snackbarMessage,
            shareLink,
            sendPrice,
            updatedPrice,
            toReview,
            nowLoading,
            refresh,
            addCart,
            buyNow
        )
    }

    //Composable wihtout viewmode (consumed live by preview)
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    // For Real Usage Not Preview
    @Composable
    fun DetailScreen(
        navigateBack: () -> Boolean,
        productDetail: Data?,
        processWish: () -> Unit,
        onWishlist: Boolean?,
        snackbarMessage: String?,
        shareLink: () -> Unit,
        updatePrice: (ProductVariant) -> Unit,
        updatedPrice: Int?,
        toReview: () -> Unit,
        nowLoading: Boolean,
        refreshFunction: () -> Unit,
        addCart: () -> Unit,
        buyNow: () -> Unit
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            topBar = { TopBar(navigateBack) }, snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }, modifier = Modifier.background(Color.White), bottomBar = {
                BottomButton(!nowLoading && productDetail != null, addCart, buyNow)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                //Snackbar DO NOT TOUCH
                if (snackbarMessage != null) {
                    LaunchedEffect(null) {
                        snackbarHostState.showSnackbar(snackbarMessage)
                    }
                }
                if (nowLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(Modifier.padding(top = 200.dp))
                    }

                } else if (productDetail != null) {
                    NormalLayout(
                        productDetail = productDetail,
                        onWishlist = onWishlist,
                        processWish = processWish,
                        shareLink = shareLink,
                        updatePrice = updatePrice,
                        updatedPrice = updatedPrice,
                        toReview = toReview
                    )
                } else {
                    //error
                    ErrorScreen(refreshFunction)

                }

            }
        }
    }

    //Preview Thingy
    @Preview(showBackground = true)
    @Composable
    fun DetailScreenPreview() {
        val previewData = null
        DetailScreen({ true }, previewData, {}, false, null,
            {}, {}, 29000, {}, false, {}, {}, {}
        )
    }


    private fun buyNow() {
        val paket = viewModel.parcelizeProduct()
        findNavController().navigate(
            R.id.StartCheckoutFromDetailCompose,
            //this way is a must if called in composable!!!!!!!!!!!!!!!!!!!
            //DO NOT USE FRAGMENT DIRECTIONS IN COMPOSABLE, EVER!!
            CheckoutFragmentArgs(paket).toBundle(),
            navOptions = null
        )
    }

}