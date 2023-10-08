package com.martabak.ecommerce.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentStatusBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat

/**
 * A simple [Fragment] subclass.
 * Use the [StatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class StatusFragment : Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatusViewModel by viewModels()
    private val args: StatusFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parcel = args.statusData
        viewModel.parcel = parcel
        initTexts(parcel)
        initObservers()
        binding.afterSaleRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            viewModel.rating = rating.toInt()
        }
        binding.confirmRatingButton.setOnClickListener {
            if (binding.ratingEditText.text.toString().isNotBlank()) {
                viewModel.review = binding.ratingEditText.text.toString()
            }

            viewModel.sendRating()
        }

        val backCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
           goBack()
        }
    }



    private fun initTexts(parcel: StatusParcel) {
        binding.apply {
            transactionID.text = parcel.invoiceId
            transactionDate.text = parcel.date
            transactionTime.text = parcel.time
            transactionPayment.text = parcel.payment
            val formattedPrice =
                NumberFormat.getInstance().format(parcel.invoiceSum).replace(",", ".")
            transactionSum.text = "Rp$formattedPrice"
        }
    }

    private fun initObservers() {
        viewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                goBack()
            }
        }
    }

    private fun goBack() {
        if(args.fromTransaction) {
            findNavController().navigateUp()
        } else {
            findNavController().navigate(R.id.action_statusFragment_to_mainFragment)
        }

    }
}
