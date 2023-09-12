package com.martabak.ecommerce.product_detail.compose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.martabak.ecommerce.R
import com.martabak.ecommerce.network.data.product_detail.Data
import com.martabak.ecommerce.network.data.product_detail.ProductVariant
import com.martabak.ecommerce.product_detail.ProductDetailViewModel
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
            val navBack = {findNavController().navigateUp()}
            setContent {

                DetailScreen(navBack)
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    // For Real Usage Not Preview
    @Composable
    fun DetailScreen(navigateBack : () -> Boolean) {
        val dataState = viewModel.productData.observeAsState()
        Scaffold(
            topBar = { TopBar(navigateBack) }) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                val pagerState = rememberPagerState()
                if (dataState.value != null) {
                    imageList = dataState.value!!.image
                    HorizontalPager(pageCount = imageList.size, state = pagerState) {
                        AsyncImage(
                            modifier = Modifier
                                .height(309.dp)
                                .fillMaxWidth(),
                            model = imageList[it],
                            contentDescription = null,
                            placeholder = painterResource(
                                id = R.drawable.thumbnail
                            ),
                            error = painterResource(id = R.drawable.thumbnail)
                        )
                    }
                }

            }
        }
    }




    @Preview(showBackground = true)
    @Composable
    fun DetailScreenPreview() {
        DetailScreen({true})
    }

}