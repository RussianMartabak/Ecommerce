package com.martabak.ecommerce.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentMainBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    val navHostFragment by lazy {
        childFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment
    }
    val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        //content here
        //set bottom nav
        binding.bottomNav.setupWithNavController(navController)

        // Inflate the layout for this fragment
        return view
    }


}