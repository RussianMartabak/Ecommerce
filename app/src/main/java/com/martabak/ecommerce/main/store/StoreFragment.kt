package com.martabak.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.martabak.ecommerce.databinding.FragmentStoreBinding
import com.martabak.ecommerce.main.store.adapter.ProductsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [StoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class StoreFragment : Fragment() {
    private var _binding : FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel : StoreViewModel by viewModels()


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
        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSearchDialog()
                v.clearFocus()

            }
        }

        val pagingAdapter = ProductsPagingAdapter()
        binding.productRecycler.adapter = pagingAdapter
        binding.productRecycler.layoutManager = LinearLayoutManager(requireActivity())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagerFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        //listen to list then add all to chips in schip group
        viewModel.filterChips.observe(viewLifecycleOwner) {filterList ->
            binding.filterChipGroup.removeAllViews()
            filterList.forEach {
                val newChip = Chip(requireActivity())
                val title = when(it) {
                    "sale" -> "Penjualan"
                    "rating" -> "Ulasan"
                    "lowest" -> "Harga Terendah"
                    "highest" -> "Harga Tertinggi"
                    else -> "null"
                }
                newChip.text = title
                binding.filterChipGroup.addView(newChip)
            }
        }

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            binding.searchEditText.setText(it)
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

    private fun showSearchDialog() {
        val fragmentManager = childFragmentManager
        val newFragment = SearchDialogFragment()
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction
            .add(newFragment, "a")
            .addToBackStack(null)
            .commit()
    }


}