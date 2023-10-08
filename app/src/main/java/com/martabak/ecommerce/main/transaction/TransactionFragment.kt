package com.martabak.ecommerce.main.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.martabak.ecommerce.databinding.FragmentTransactionBinding
import com.martabak.ecommerce.main.MainFragment
import com.martabak.ecommerce.main.MainFragmentDirections
import com.martabak.ecommerce.main.transaction.adapters.TransactionAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class TransactionFragment : Fragment() {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingCircle.isVisible = false
        binding.errorLayout.isVisible = false
        val adapter = TransactionAdapter {
            val parcel = viewModel.getStatusParcel(it)
            val aktion = MainFragmentDirections.startStatusFromTransaction(parcel, true)
            val mainFragment = requireParentFragment().parentFragment as MainFragment
            mainFragment.findNavController().navigate(aktion)
        }
        binding.transactionRecycler.adapter = adapter
        binding.transactionRecycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.refreshButton.setOnClickListener {
            viewModel.getTransactions()
        }

        viewModel.nowLoading.observe(viewLifecycleOwner) {
            binding.loadingCircle.isVisible = it
        }

        viewModel.transactionList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            if (it.isEmpty()) {
                binding.transactionRecycler.isVisible = false
                binding.errorLayout.isVisible = true
            } else {
                binding.transactionRecycler.isVisible = true
                binding.errorLayout.isVisible = false
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getTransactions()
    }
}
