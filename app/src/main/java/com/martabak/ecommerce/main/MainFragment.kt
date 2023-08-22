package com.martabak.ecommerce.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentMainBinding
import com.martabak.ecommerce.utils.SharedPrefKeys.getUsername
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userPref : SharedPreferences


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
        Log.d("zaky", "Current username = ${userPref.getUsername()}")
        binding.Toolbar.title = userPref.getUsername()

        // Inflate the layout for this fragment
        return view
    }


}