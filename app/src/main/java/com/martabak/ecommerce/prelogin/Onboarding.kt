package com.martabak.ecommerce.prelogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.martabak.ecommerce.R
import com.martabak.ecommerce.adapters.OnboardingAdapter
import com.martabak.ecommerce.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [Onboarding.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class Onboarding : Fragment() {


    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        //content here
        if (!viewModel.isFirst) {
            findNavController().navigate(R.id.action_onboarding_to_loginFragment)
        }
        var pagerIndex = 0
        var onboardAdapter = OnboardingAdapter()
        var onboardingPager = binding.onboardPager
        onboardingPager.adapter = onboardAdapter
        var pagerCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == 2) {
                    binding.nextButton.visibility = View.INVISIBLE
                }
            }
        }
        onboardingPager.registerOnPageChangeCallback(pagerCallback)

        //hook with "indicator"
        TabLayoutMediator(binding.indicator, binding.onboardPager) { tab, position ->

        }.attach()


        binding.skipButton.setOnClickListener {
            viewModel.registerInstall()
            view.findNavController().navigate(R.id.action_onboarding_to_loginFragment)
        }

        binding.toRegisterButton.setOnClickListener {
            viewModel.registerInstall()
            view.findNavController().navigate(R.id.action_onboarding_to_registerFragment)
        }

        binding.nextButton.setOnClickListener {
            pagerIndex += 1
            if (pagerIndex < 2) {
                onboardingPager.setCurrentItem(pagerIndex)
            } else {
                binding.nextButton.visibility = View.INVISIBLE
                onboardingPager.setCurrentItem(pagerIndex)
            }

        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}