package com.martabak.ecommerce.main.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentFilterDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilterDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: StoreViewModel by activityViewModels()
    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var analytics: FirebaseAnalytics

    // list for analytics
    val filters = arrayOf("rating", "sale", "lowest", "highest", "lowest", "Apple", "Asus", "Dell", "Lenovo")

    // filter vars
    private var brand: String? = null
    private var lowest: Int? = null
    private var highest: Int? = null
    private var sort: String? = null

    // for display
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
        // reset shit
        binding.resetButton.isVisible = isFilterSelected()

        // pre filling
        viewModel.selectedSort?.let {
            binding.sortGroup.check(it)
        }
        sort = viewModel.sort
        viewModel.sort?.let { sortText = convertKeytoSort(it) }
        viewModel.selectedBrand?.let {
            binding.brandGroup.check(it)
        }
        brand = viewModel.brand
        viewModel.brand?.let {
            brandText = it.replaceFirstChar { it.uppercase() }
        }
        viewModel.lowest?.let {
            binding.editTextLowest.setText(it.toString())
        }
        viewModel.highest?.let {
            binding.editTextHighest.setText(it.toString())
        }
        // reset everything
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
            binding.resetButton.isVisible = false
        }

        // listeners for data input
        binding.editTextLowest.doOnTextChanged { text, _, _, _ ->
            binding.resetButton.isVisible = text != ""
        }

        binding.editTextHighest.doOnTextChanged { text, _, _, _ ->
            binding.resetButton.isVisible = text != ""
        }

        binding.sortGroup.setOnCheckedStateChangeListener { group, checkedId ->
            if (checkedId.size != 0) {
                // send id to viewmodel
                // send sort http key to fragment
                // send sort string to viewmodel for making a string list
                var selectedChip = group.findViewById<Chip>(checkedId[0])
                viewModel.selectedSort = selectedChip.id
                // text thats on view already, send to filterchips livedata
                sortText = selectedChip.text.toString()
                // analytics shit
                logToFirebase(0, "sort")

                // value thats needed for http ops nad send back to fragment
                sort = convertSorttoKey(sortText)
                binding.resetButton.isVisible = true
            }
        }
        binding.brandGroup.setOnCheckedStateChangeListener { group, checkedId ->
            if (!(checkedId.size == 0)) {
                var selectedChip = group.findViewById<Chip>(checkedId[0])
                viewModel.selectedBrand = selectedChip.id
                brandText = selectedChip.text.toString()
                brand = brandText.lowercase()

                logToFirebase(1, "brand")
                binding.resetButton.isVisible = true
            }
        }

        binding.sendButton.setOnClickListener {
            // make the list
            if (binding.editTextLowest.text.toString().isNotEmpty()) {
                lowest =
                    binding.editTextLowest.text.toString().toInt()
            }
            if (binding.editTextHighest.text.toString().isNotEmpty()) {
                highest =
                    binding.editTextHighest.text.toString().toInt()
            }
            viewModel.updateFilterChipList(makeFilterList())
            Log.d("zaky", "on dialog fragment Highest: $highest , Lowest: $lowest")
            setFragmentResult(
                "filters",
                bundleOf(
                    "sortKey" to sort,
                    "brandKey" to brand,
                    "lowestKey" to lowest,
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
            "Review" -> "rating"
            "Sale" -> "sale"
            "Lowest Price" -> "lowest"
            "Highest Price" -> "highest"
            else -> ""
        }
    }

    private fun logToFirebase(catId: Int, catName: String) {
        val itemList = mutableListOf<Bundle>()
        filters.forEach { filtername ->
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_NAME, filtername)
            }
            itemList.add(bundle)
        }
        val itemArray = itemList.toTypedArray()
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEMS, itemArray)
            param(FirebaseAnalytics.Param.ITEM_LIST_ID, catId.toLong())
            param(FirebaseAnalytics.Param.ITEM_LIST_NAME, catName)
        }
    }

    fun convertKeytoSort(text: String): String {
        return when (text) {
            "rating" -> "Ulasan"
            "sale" -> "Penjualan"
            "lowest" -> "Harga Terendah"
            "highest" -> "Harga Tertinggi"
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

    private fun isFilterSelected(): Boolean {
        return (viewModel.brand != null || viewModel.sort != null || viewModel.highest != null || viewModel.lowest != null)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}
