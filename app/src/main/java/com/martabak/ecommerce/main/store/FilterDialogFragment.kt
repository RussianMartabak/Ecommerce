package com.martabak.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentFilterDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: StoreViewModel by activityViewModels()
    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!

    //filter vars
    private var brand: String? = null
    private var lowest: Int? = null
    private var highest: Int? = null
    private var sort: String? = null

    //for display
    private var sortText: String = ""
    private var brandText: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val themeWrapper = ContextThemeWrapper(requireActivity(), R.style.Base_Theme_Ecommerce)
        _binding = FragmentFilterDialogBinding.inflate(
            inflater.cloneInContext(themeWrapper),
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //pre filling
        viewModel.selectedSort?.let {
            binding.sortGroup.check(it)
        }
        viewModel.selectedBrand?.let {
            binding.brandGroup.check(it)
        }
        viewModel.lowest?.let {
            binding.editTextLowest.setText(it.toString())
        }
        viewModel.highest?.let {
            binding.editTextHighest.setText(it.toString())
        }
        //reset everything
        binding.resetButton.setOnClickListener {
            brand = null
            highest = null
            lowest = null
            sort = null
            viewModel.resetFilters()
            binding.brandGroup.clearCheck()
            binding.sortGroup.clearCheck()
            binding.editTextHighest.setText("")
            binding.editTextLowest.setText("")
        }

        //listeners for data input
        binding.sortGroup.setOnCheckedStateChangeListener { group, checkedId ->
            if (!(checkedId.size == 0)) {
                //send id to viewmodel
                //send sort http key to fragment
                //send sort string to viewmodel for making a string list
                var selectedChip = group.findViewById<Chip>(checkedId[0])
                viewModel.selectedSort = selectedChip.id
                //text thats on view already, send to filterchips livedata
                sortText = selectedChip.text.toString()
                //value thats needed for http ops nad send back to fragment
                sort = convertSorttoKey(sortText)
            }

        }
        binding.brandGroup.setOnCheckedStateChangeListener { group, checkedId ->
            if (!(checkedId.size == 0)) {
                var selectedChip = group.findViewById<Chip>(checkedId[0])
                viewModel.selectedBrand = selectedChip.id
                brandText = selectedChip.text.toString()
                brand = brandText.lowercase()
            }

        }

        binding.sendButton.setOnClickListener {
            //make the list
            if (binding.editTextLowest.text.toString().isNotEmpty()) lowest =
                binding.editTextLowest.text.toString().toInt()
            if (binding.editTextHighest.text.toString().isNotEmpty()) highest =
                binding.editTextHighest.text.toString().toInt()
            viewModel.updateFilterChipList(makeFilterList())
            setFragmentResult(
                "filters",
                bundleOf(
                    "sortKey" to sort,
                    "brandKey" to brand,
                    "lowestKet" to lowest,
                    "highestKey" to highest
                )
            )
            dismiss()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun convertSorttoKey(text: String): String {
        return when (text) {
            "Ulasan" -> "rating"
            "Penjualan" -> "sale"
            "Harga Terendah" -> "lowest"
            "Harga Tertinggi" -> "highest"
            else -> ""
        }
    }

    private fun makeFilterList(): List<String> {
        val list = mutableListOf<String>()
        sort?.let { list.add(sortText) }
        brand?.let { list.add(brandText) }
        lowest?.let { list.add("< $it") }
        highest?.let { list.add("> $it") }
        return list
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}