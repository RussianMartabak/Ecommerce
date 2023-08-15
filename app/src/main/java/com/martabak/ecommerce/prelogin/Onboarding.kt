package com.martabak.ecommerce.prelogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.martabak.ecommerce.R
import com.martabak.ecommerce.adapters.OnboardingAdapter
import com.martabak.ecommerce.databinding.FragmentOnboardingBinding



/**
 * A simple [Fragment] subclass.
 * Use the [Onboarding.newInstance] factory method to
 * create an instance of this fragment.
 */
class Onboarding : Fragment() {
    val navHostFragment by lazy{
        childFragmentManager.findFragmentById(R.id.rootNavHost) as NavHostFragment
    }

    private var _binding : FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        //content here
        var onboardAdapter = OnboardingAdapter()
        var onboardingPager = binding.onboardPager
        onboardingPager.adapter = onboardAdapter

        //hook with "indicator"
        TabLayoutMediator(binding.indicator, binding.onboardPager) {tab, position ->

        }.attach()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}