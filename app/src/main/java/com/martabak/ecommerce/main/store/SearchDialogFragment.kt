package com.martabak.ecommerce.main.store

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentStoreDialogBinding

class SearchDialogFragment : DialogFragment() {
    // set le layout

    private var _binding: FragmentStoreDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    //called when creating layout
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchEditText.requestFocus()
        super.onViewCreated(view, savedInstanceState)
    }
}