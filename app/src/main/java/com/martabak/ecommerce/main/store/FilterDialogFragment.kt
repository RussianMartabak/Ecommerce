package com.martabak.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentFilterDialogBinding

class FilterDialogFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val themeWrapper = ContextThemeWrapper(requireActivity(), R.style.Base_Theme_Ecommerce)
        _binding = FragmentFilterDialogBinding.inflate(inflater.cloneInContext(themeWrapper), container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}