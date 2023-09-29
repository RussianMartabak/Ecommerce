package com.martabak.ecommerce.main.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentStoreBinding
import com.martabak.ecommerce.main.MainFragment
import com.martabak.ecommerce.main.store.adapter.ProductsLoadStateAdapter
import com.martabak.ecommerce.main.store.adapter.ProductsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [StoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class StoreFragment : Fragment() {
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by activityViewModels()

    // params for bottomsheet
    private var brand: String? = null
    private var lowest: Int? = null
    private var highest: Int? = null
    private var sort: String? = null
    private var gridMode = false
    private var pagingAdapter: ProductsPagingAdapter? = null

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.cancelProductViewing()
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val grandParentFrag =
            (this.requireParentFragment() as NavHostFragment).requireParentFragment()
        binding.gridShimmerLayout.isVisible = false
        binding.linearShimmerLayout.isVisible = false
        binding.errorLayout.isVisible = false
        val fragmentManager = childFragmentManager
        // FM listeners
        fragmentManager.setFragmentResultListener(
            "searchKey",
            viewLifecycleOwner
        ) { _, bundle ->
            // listen to search query change
            val result = bundle.getString("searchKey")
            viewModel.setSearch(result!!)
            binding.searchEditText.setText(result)
            Log.d("zaky", "Fragment result is $result")
        }
        // listen for bottomsheet value
        fragmentManager.setFragmentResultListener(
            "filters",
            viewLifecycleOwner
        ) { _, bundle ->
            sort = bundle.getString("sortKey")
            brand = bundle.getString("brandKey")
            lowest = if (bundle.getInt("lowestKey") == 0) null else bundle.getInt("lowestKey")
            highest = if (bundle.getInt("highestKey") == 0) null else bundle.getInt("highestKey")
            Log.d("zaky", "on StoreFragment Highest: $highest, Lowest: $lowest")
            viewModel.setFilters(_brand = brand, _sort = sort, _lowest = lowest, _highest = highest)
        }

        binding.searchEditText.setOnClickListener {
            showSearchDialog(viewModel.query)
        }

        pagingAdapter = ProductsPagingAdapter { id ->
            // store product ID on repo then move away from this scheisse
            Log.d("zaky", "selected ID: $id")
            viewModel.selectProductID(id)
            // log le selected item
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                param(FirebaseAnalytics.Param.ITEM_LIST_ID, 3)
                param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Products")
            }
            (grandParentFrag as MainFragment).findNavController()
                .navigate(R.id.action_mainFragment_to_productDetailComposeFragment)
        }
        binding.productRecycler.adapter =
            pagingAdapter!!.withLoadStateFooter(ProductsLoadStateAdapter())
        binding.productRecycler.layoutManager = GridLayoutManager(requireActivity(), 1)
        // listen to recycler load state
        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter!!.loadStateFlow.collectLatest { loadStates ->

                if (loadStates.refresh is LoadState.Loading) {
                    showLoading()
                } else if (loadStates.refresh is LoadState.Error) {
                    val error = loadStates.refresh as LoadState.Error
                    showError(error)
                } else {
                    binding.productRecycler.isVisible = true
                    binding.errorLayout.isVisible = false
                    binding.linearShimmerLayout.isVisible = false
                    binding.gridShimmerLayout.isVisible = false
                }
            }
        }
        binding.gridSelector.setOnClickListener {
            gridMode = !gridMode
            Log.d("zaky", "switching to gridMode $gridMode")
            switchLayout()
        }

        viewModel.updatedPagingSource.observe(viewLifecycleOwner) { pagingData ->
            viewLifecycleOwner.lifecycleScope.launch {
                pagingAdapter!!.submitData(pagingData)
            }
        }

        // listen to coordinate shimmer

        binding.swiper.setOnRefreshListener {
            pagingAdapter!!.refresh()
            binding.swiper.isRefreshing = false
        }

        // listen to list then add all to chips in schip group
        viewModel.filterChips.observe(viewLifecycleOwner) { filterList ->
            binding.filterChipGroup.removeAllViews()
            filterList.forEach {
                val newChip = Chip(requireActivity(), null, com.google.android.material.R.attr.chipStandaloneStyle)
                val title = it
                newChip.text = title
                binding.filterChipGroup.addView(newChip)
            }
        }

        binding.filterChip.setOnClickListener {
            showBottomSheet()
        }
        // content here boys
    }

    override fun onResume() {
        super.onResume()
        switchLayout()
    }

    private fun switchLayout() {
        if (gridMode) {
            binding.gridSelector.setImageResource(R.drawable.grid_view)
            val gridManager = GridLayoutManager(requireActivity(), 2)
            val footerAdapter = ProductsLoadStateAdapter()
            // change recycler to grid
            pagingAdapter!!.setGridMode(true)
            binding.productRecycler.layoutManager = gridManager
            binding.productRecycler.adapter =
                pagingAdapter!!.withLoadStateFooter(footerAdapter)
            gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == pagingAdapter!!.itemCount && footerAdapter.itemCount > 0) {
                        2
                    } else {
                        1
                    }
                }
            }
        } else {
            binding.gridSelector.setImageResource(R.drawable.format_list_bulleted)
            // change to linear
            pagingAdapter!!.setGridMode(false)
            binding.productRecycler.layoutManager = GridLayoutManager(requireActivity(), 1)
            binding.productRecycler.adapter =
                pagingAdapter!!.withLoadStateFooter(ProductsLoadStateAdapter())
        }
    }

    private fun showBottomSheet() {
        val modalBottomSheet = FilterDialogFragment()
        modalBottomSheet.show(childFragmentManager, FilterDialogFragment.TAG)
    }

    private fun showSearchDialog(q: String) {
        val newFragment = SearchDialogFragment().newInstance(q)
        val transaction = childFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction
            .add(newFragment, "a")
            .addToBackStack(null)
            .commit()
    }

    private fun showLoading() {
        if (gridMode) {
            binding.gridShimmerLayout.isVisible = true
        } else {
            binding.linearShimmerLayout.isVisible = true
        }

        binding.productRecycler.isVisible = false
        binding.errorLayout.isVisible = false
    }

    private fun showError(errorState: LoadState.Error) {
        binding.errorLayout.isVisible = true
        binding.productRecycler.isVisible = false
        binding.linearShimmerLayout.isVisible = false
        binding.gridShimmerLayout.isVisible = false

        if (errorState.error is HttpException) {
            val httpError = errorState.error as HttpException
            if (httpError.code() == 404) {
                binding.errorTitle.text = "Empty"
                binding.errorDetail.text = "Your requested data is unavailable"
                binding.refreshButton.text = "Reset"
                binding.refreshButton.setOnClickListener {
                    viewModel.resetFilters()
                    viewModel.query = ""
                    binding.searchEditText.setText("")
                    viewModel.callRefresh()
                }
            } else {
                binding.refreshButton.setOnClickListener {
                    pagingAdapter!!.refresh()
                }
                binding.errorTitle.text = httpError.code().toString()
                binding.errorDetail.text = "Internal Server Error"
            }
        } else {
            binding.refreshButton.setOnClickListener {
                pagingAdapter!!.refresh()
            }
            binding.errorTitle.text = "Connection"
            binding.errorDetail.text = "Your connection is unavailable"
        }
    }

    // Functiom to change the edittext text
    //
}
