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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.martabak.ecommerce.R
import com.martabak.ecommerce.checkout.CheckoutFragmentArgs
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.network.data.product_detail.ProductVariant
import com.martabak.ecommerce.product_detail.ProductDetailViewModel
import com.martabak.ecommerce.product_detail.compose.product_detail_components.BottomButton
import com.martabak.ecommerce.product_detail.compose.product_detail_components.ErrorScreen
import com.martabak.ecommerce.product_detail.compose.product_detail_components.NormalLayout
import com.martabak.ecommerce.product_detail.compose.product_detail_components.TopBar
import com.martabak.ecommerce.ui.theme.EcommerceTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


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
    private val args : ProductDetailComposeFragmentArgs by navArgs()
    @Inject
    lateinit var analytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.ID?.let {
            viewModel.setProductID(it)
        }
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
                EcommerceTheme {
                    DetailScreen(viewModel = viewModel) {
                        buyNow()
                    }
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
            }, bottomBar = {
                BottomButton(!nowLoading && productDetail != null, addCart, buyNow)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues)

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
                    //make product data as bundle
                    val bundle = Bundle()
                    bundle.putString("name", productDetail.productName)

                    //Log event
                    analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
                        param(FirebaseAnalytics.Param.CURRENCY, "IDR")
                        param(FirebaseAnalytics.Param.VALUE, productDetail.productPrice.toDouble())
                        param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundle))
                    }

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