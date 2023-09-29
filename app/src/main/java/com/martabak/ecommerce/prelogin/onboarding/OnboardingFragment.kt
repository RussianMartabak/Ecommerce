package com.martabak.ecommerce.prelogin.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentOnboardingBinding
import com.martabak.ecommerce.utils.GlobalUtils.logButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class OnboardingFragment() : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels()

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var pagerIndex = 0
        val onboardAdapter = OnboardingAdapter()
        val onboardingPager = binding.onboardPager
        onboardingPager.adapter = onboardAdapter

        val pagerCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                pagerIndex = position
                if (position == 2) {
                    binding.nextButton.visibility = View.INVISIBLE
                } else {
                    binding.nextButton.visibility = View.VISIBLE
                }
            }
        }
        onboardingPager.registerOnPageChangeCallback(pagerCallback)

        // hook with "indicator"
        TabLayoutMediator(binding.indicator, binding.onboardPager) { _, _ -> }.attach()

        binding.skipButton.setOnClickListener {
            viewModel.registerInstall()
            analytics.logButton("skip")
            view.findNavController().navigate(R.id.action_onboarding_to_loginFragment)
        }

        binding.toRegisterButton.setOnClickListener {
            viewModel.registerInstall()
            analytics.logButton("register")
            view.findNavController().navigate(R.id.action_onboarding_to_registerFragment)
        }

        binding.nextButton.setOnClickListener {
            analytics.logButton("next")
            pagerIndex += 1
            if (pagerIndex < 3) {
                onboardingPager.currentItem = pagerIndex
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
