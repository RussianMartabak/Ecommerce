package com.martabak.ecommerce.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.martabak.ecommerce.R
import com.martabak.ecommerce.checkout.adapters.PaymentParentAdapter
import com.martabak.ecommerce.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PaymentFragment : Fragment() {
    private var _binding : FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val viewModel : PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        viewModel.getPaymentData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PaymentParentAdapter { label, image ->
            setFragmentResult("paymentMethod", bundleOf(
                "methodName" to label,
                "methodImage" to image
            ))
            findNavController().navigateUp()
        }
        binding.paymentRecyclerParent.apply {
            setAdapter(adapter)
            layoutManager = LinearLayoutManager(requireActivity())

        }
        viewModel.paymentData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

   
}