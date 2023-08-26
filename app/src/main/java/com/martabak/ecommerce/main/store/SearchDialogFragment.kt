package com.martabak.ecommerce.main.store

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentStoreDialogBinding
import com.martabak.ecommerce.main.store.adapter.SearchListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDialogFragment : DialogFragment() {
    // set le layout

    private var _binding: FragmentStoreDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchDialogViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        var query = ""
        viewModel.searchQuery.observe(viewLifecycleOwner) {
            binding.searchEditText.setText(it)
            binding.searchEditText.requestFocus()
        }
        val searchItemListAdapter = SearchListAdapter {
            viewModel.sendQuery(it)
            dismiss()
        }

        binding.searchEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.sendQuery(binding.searchEditText.text.toString())
                dismiss()
                return@OnKeyListener true
            }
            false
        })

        binding.searchEditText.requestFocus()
        binding.loadingIndicator.visibility = View.GONE
        val bounceManager = object : CountDownTimer(1000, 100) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                Log.d("zaky", "Hitting API right now")
                viewModel.getSearchItems(query)
            }

        }


        binding.searchRecyclerView.adapter = searchItemListAdapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        //loading stuffs
        viewModel.nowLoading.observe(viewLifecycleOwner) {
            if (it) binding.loadingIndicator.visibility =
                View.VISIBLE else binding.loadingIndicator.visibility = View.GONE
        }

        viewModel.items.observe(viewLifecycleOwner) {
            searchItemListAdapter.submitList(it)

        }





        binding.searchEditText.doOnTextChanged { text, start, _, _ ->
            query = text.toString()
            bounceManager.cancel()
            bounceManager.start()
        }
        super.onViewCreated(view, savedInstanceState)
    }
}