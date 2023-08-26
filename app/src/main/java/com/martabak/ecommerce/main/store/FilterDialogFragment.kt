package com.martabak.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentFilterDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: FilterDialogViewModel by viewModels()
    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!
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
        var brand : String? = null
        var lowest : Int? = null
        var highest : Int? = null
        var sort : String? = null


        binding.sortGroup.setOnCheckedStateChangeListener { group, checkedId ->
            var value = group.findViewById<Chip>(checkedId[0]).text.toString()
            when(value) {
                "Ulasan" -> sort = "rating"
                "Penjualan" -> sort = "sale"
                "Harga Terendah" -> sort = "lowest"
                "Harga Tertinggi" -> sort = "highest"
            }
        }

        binding.sendButton.setOnClickListener {
            viewModel.sendProductQuery(brand, lowest, highest, sort)
            dismiss()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}