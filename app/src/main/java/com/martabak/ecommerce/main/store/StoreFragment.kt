package com.martabak.ecommerce.main.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentStoreBinding
import com.martabak.ecommerce.main.store.adapter.ProductsLoadStateAdapter
import com.martabak.ecommerce.main.store.adapter.ProductsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException


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
    //params for bottomsheet
    private var brand : String? = null
    private var lowest : Int? = null
    private var highest : Int? = null
    private var sort : String? = null
    private var gridMode = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linearShimmerLayout.isVisible = false
        binding.errorLayout.isVisible = false
        val fragmentManager = childFragmentManager
        //FM listeners
        fragmentManager.setFragmentResultListener(
            "searchKey",
            viewLifecycleOwner
        ) { _, bundle ->
            //listen to search query change
            val result = bundle.getString("searchKey")
            viewModel.setSearch(result!!)
            binding.searchEditText.setText(result)
            Log.d("zaky", "Fragment result is $result")
        }
        //listen for bottomsheet value
        fragmentManager.setFragmentResultListener(
            "filters",
            viewLifecycleOwner
        ) {_, bundle ->
            sort = bundle.getString("sortKey")
            brand = bundle.getString("brandKey")
            lowest = bundle.getInt("lowestKey")
            highest = bundle.getInt("highestKey")
            viewModel.setFilters(_brand = brand, _sort = sort, _lowest = lowest, _highest = highest)
        }

        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSearchDialog(viewModel.query)
                v.clearFocus()

            }
        }

        binding.gridSelector.setOnClickListener {
            gridMode = !gridMode
            Log.d("zaky", "switching to gridMode ${gridMode}")
            if (gridMode) {
                binding.gridSelector.setImageResource(R.drawable.grid_view)
            } else {
                binding.gridSelector.setImageResource(R.drawable.format_list_bulleted)
            }
        }

        val pagingAdapter = ProductsPagingAdapter()
        binding.productRecycler.adapter = pagingAdapter.withLoadStateFooter(ProductsLoadStateAdapter())
        binding.productRecycler.layoutManager = GridLayoutManager(requireActivity(), 1)
        //listen to recycler load state
        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->

                if (loadStates.refresh is LoadState.Loading) {
                    showLoading()
                } else if (loadStates.refresh is LoadState.Error) {
                    val error = loadStates.refresh as LoadState.Error
                    showError(error)
                } else {
                    binding.productRecycler.isVisible = true
                    binding.errorLayout.isVisible = false
                    binding.linearShimmerLayout.isVisible = false
                }




            }
        }

        viewModel.updatedPagingSource.observe(viewLifecycleOwner) { pagingData ->
            viewLifecycleOwner.lifecycleScope.launch {
                pagingAdapter.submitData(pagingData)

            }
        }

        //listen to coordinate shimmer


        binding.swiper.setOnRefreshListener {

            pagingAdapter.refresh()
            binding.swiper.isRefreshing = false
        }

        binding.refreshButton.setOnClickListener {
            pagingAdapter.refresh()
        }



        //listen to list then add all to chips in schip group
        viewModel.filterChips.observe(viewLifecycleOwner) { filterList ->
            binding.filterChipGroup.removeAllViews()
            filterList.forEach {
                val newChip = Chip(requireActivity())
                val title = it
                newChip.text = title
                binding.filterChipGroup.addView(newChip)
            }
        }


        binding.filterChip.setOnClickListener {
            showBottomSheet()
        }
        //content here boys

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
        binding.linearShimmerLayout.isVisible = true
        binding.productRecycler.isVisible = false
        binding.errorLayout.isVisible = false
    }

    private fun showError(errorState : LoadState.Error) {
        binding.errorLayout.isVisible = true
        binding.productRecycler.isVisible = false
        binding.linearShimmerLayout.isVisible = false

        if(errorState.error is HttpException) {
            val httpError = errorState.error as HttpException
            if(httpError.code() == 404) {
                binding.errorTitle.text = "Empty"
                binding.errorDetail.text = "Your requested data is unavailable"
            } else {
                binding.errorTitle.text = httpError.code().toString()
                binding.errorDetail.text = "Internal Server Error"
            }
        } else {
            binding.errorTitle.text = "Connection"
            binding.errorDetail.text = "Your connection is unavailable"
        }
    }

    //Functiom to change the edittext text
    //


}