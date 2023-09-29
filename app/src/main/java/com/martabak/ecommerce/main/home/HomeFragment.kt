package com.martabak.ecommerce.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.martabak.ecommerce.MainActivity
import com.martabak.ecommerce.databinding.FragmentHomeBinding
import com.martabak.ecommerce.utils.GlobalUtils.nightMode
import com.martabak.ecommerce.utils.GlobalUtils.setNightMode
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            (requireActivity() as MainActivity).logout()
        }

        val langTag = (requireActivity() as MainActivity).getLang()
        Log.d("zaky", "current language tag : $langTag")
        binding.langSwitch.isChecked = false
        (requireActivity() as MainActivity).switchLang("en")

        binding.langSwitch.setOnClickListener {
            if (binding.langSwitch.isChecked) {
                (requireActivity() as MainActivity).switchLang("in")
            } else {
                (requireActivity() as MainActivity).switchLang("en")
            }
        }

        binding.themeSwitch.isChecked = viewModel.sharedPreferences.nightMode()

        // theme switch
        binding.themeSwitch.setOnClickListener {
            Log.d("zaky", "theme switch clicked")
            if (binding.themeSwitch.isChecked) {
                activity.setAppTheme(true)
                viewModel.sharedPreferences.setNightMode(true)
            } else {
                viewModel.sharedPreferences.setNightMode(false)
                activity.setAppTheme(false)
            }
        }
    }
}
