package com.martabak.ecommerce.product_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentReviewBinding
import com.martabak.ecommerce.product_detail.adapters.ReviewAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [ReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ReviewFragment : Fragment() {
    private var _binding : FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ReviewViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingCircle.isVisible = true
        viewModel.getReviews()
        binding.topBarDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.reviewData.observe(viewLifecycleOwner) {reviewList ->
            binding.loadingCircle.isVisible = false
            val reviewAdapter = ReviewAdapter(reviewList)
            binding.recyclerReview.adapter = reviewAdapter
            binding.recyclerReview.layoutManager = LinearLayoutManager(requireActivity())
        }
    }


}