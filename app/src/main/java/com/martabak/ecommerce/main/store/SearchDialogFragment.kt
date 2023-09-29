package com.martabak.ecommerce.main.store

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
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

    // params
    private var searchKey = ""
    private val viewModel: StoreViewModel by activityViewModels()
    fun newInstance(q: String): SearchDialogFragment {
        searchKey = q
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    // called when creating layout
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var query = ""
        binding.searchEditText.setText(searchKey)
        binding.searchEditText.requestFocus()

        val searchItemListAdapter = SearchListAdapter {
            setFragmentResult(
                "searchKey",
                bundleOf("searchKey" to it)
            )
            dismiss()
        }
        // should transfer the search key to store frag when enter is pressed
        binding.searchEditText.setOnKeyListener(
            View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    query = binding.searchEditText.text.toString()

                    setFragmentResult("searchKey", bundleOf("searchKey" to query))

                    dismiss()
                    return@OnKeyListener true
                }
                false
            }
        )

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
        // loading stuffs
        viewModel.nowLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingIndicator.visibility =
                    View.VISIBLE
            } else {
                binding.loadingIndicator.visibility = View.GONE
            }
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
