package com.martabak.ecommerce.status

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val viewModel : StatusViewModel by viewModels()
    private val args: StatusFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
            viewModel.review = binding.ratingEditText.text.toString()
            viewModel.sendRating()
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
                findNavController().navigateUp()
            }
        }
    }


}