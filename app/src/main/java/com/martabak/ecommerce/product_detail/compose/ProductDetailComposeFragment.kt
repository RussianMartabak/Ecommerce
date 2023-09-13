package com.martabak.ecommerce.product_detail.compose

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.martabak.ecommerce.R
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.network.data.product_detail.ProductVariant
import com.martabak.ecommerce.product_detail.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat


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
                DetailScreen(viewModel = viewModel)
            }
        }
    }

    @Composable
    fun DetailScreen(viewModel: ProductDetailViewModel) {
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
        DetailScreen(
            navBack,
            productData,
            processWish,
            productOnWishlist,
            snackbarMessage,
            shareLink,
            sendPrice,
            updatedPrice
        )
    }

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
        updatedPrice : Int?
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            topBar = { TopBar(navigateBack) }, snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }, modifier = Modifier.background(Color.White)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                if (snackbarMessage != null) {
                    LaunchedEffect(null) {
                        snackbarHostState.showSnackbar(snackbarMessage)
                    }
                }
                if (productDetail != null) {
                    NormalLayout(
                        productDetail = productDetail,
                        onWishlist = onWishlist,
                        processWish = processWish,
                        shareLink = shareLink,
                        updatePrice = updatePrice,
                        updatedPrice = updatedPrice
                    )
                }

            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DetailScreenPreview() {
        val previewData = Data(
            productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
            productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
            productPrice = 24499000,
            image = listOf(
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/0cc3d06c-b09d-4294-8c3f-1c37e60631a6.jpg",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/33a06657-9f88-4108-8676-7adafaa94921.jpg"
            ),
            brand = "Asus",
            description = "ASUS ROG Strix ",
            store = "AsusStore",
            sale = 12,
            stock = 2,
            totalRating = 7,
            totalReview = 5,
            totalSatisfaction = 100,
            productRating = 5.0,
            productVariant = listOf(
                ProductVariant(variantName = "RAM 16GB", variantPrice = 0),
                ProductVariant(variantName = "RAM 32GB", variantPrice = 100000)
            )
        )
        DetailScreen({ true }, previewData, {}, false, null, {}, {}, 29000)
    }


}