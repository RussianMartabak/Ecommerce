package com.martabak.ecommerce.product_detail.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.martabak.core.network.data.product_detail.ReviewData

import com.martabak.ecommerce.product_detail.ReviewViewModel
import com.martabak.ecommerce.ui.theme.EcommerceTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [ReviewComposeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ReviewComposeFragment : Fragment() {
    private val viewModel: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getReviews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                EcommerceTheme {
                    ReviewScreen(viewModel = viewModel)
                }
            }
        }
    }

    // composable for passing only viewmodel and function
    @Composable
    fun ReviewScreen(viewModel: ReviewViewModel) {
        val navigateBack = {
            findNavController().navigateUp()
        }
        val reviewList by viewModel.reviewData.observeAsState()
        ReviewScreen(navigateBack, reviewList)
    }

    // the real composable
    @Composable
    fun ReviewScreen(navigateBack: () -> Boolean, reviewList: List<ReviewData>?) {
        Scaffold(topBar = {
            TopBarReview(navigateBack)
        }) { paddingValues ->
            if (reviewList != null) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(reviewList.size) { reviewIndex ->
                        ReviewItem(data = reviewList[reviewIndex])
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun ReviewScreenPreview() {
        ReviewScreen({ true }, sampleReviewData)
    }
}
