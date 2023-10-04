package com.martabak.ecommerce.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentMainBinding
import com.martabak.ecommerce.utils.GlobalUtils.getUsername
import com.martabak.ecommerce.utils.GlobalUtils.hasUsername
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    val args: MainFragmentArgs by navArgs()

    @Inject
    lateinit var userPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // kick to profile if not has username yet
        if (!viewModel.userPref.hasUsername()) {
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.fromNotif) {
            findNavController().navigate(R.id.action_mainFragment_to_notificationFragment)
        }
        binding.Toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.cart -> {
                    findNavController().navigate(R.id.action_mainFragment_to_cartFragment)
                    true
                }
                R.id.notif -> {
                    findNavController().navigate(R.id.action_mainFragment_to_notificationFragment)
                    true
                }
                R.id.menu -> {
                    findNavController().navigate(R.id.action_mainFragment_to_screenFragment)
                    true
                }
                else -> false
            }
        }

        var navHost = childFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNav?.setupWithNavController(navController)
        binding.navRail?.setupWithNavController(navController)
        binding.navView?.setupWithNavController(navController)

        val cartBadge = BadgeDrawable.create(requireActivity())
        cartBadge.isVisible = false
        BadgeUtils.attachBadgeDrawable(cartBadge, binding.Toolbar, R.id.cart)
        viewModel.cartItemCount.observe(viewLifecycleOwner) {
            if (it > 0) {
                cartBadge.isVisible = true
                cartBadge.number = it
            } else {
                cartBadge.isVisible = false
            }
        }

        // attach badge to topbar for notif
        val notifBadge = BadgeDrawable.create(requireActivity())
        notifBadge.isVisible = false
        BadgeUtils.attachBadgeDrawable(notifBadge, binding.Toolbar, R.id.notif)
        viewModel.updatedNotifCount.observe(viewLifecycleOwner) {
            if (it > 0) {
                notifBadge.isVisible = true
                notifBadge.number = it
            } else {
                notifBadge.isVisible = false
            }
        }

        val bottomBadge = if (binding.bottomNav != null) {
            binding.bottomNav!!.getOrCreateBadge(R.id.wishlistFragment)
        } else if (binding.navRail != null) {
            binding.navRail!!.getOrCreateBadge(R.id.wishlistFragment)
        } else {
            null
        }
        bottomBadge?.isVisible = false
        viewModel.wishItemCount.observe(viewLifecycleOwner) {
            Log.d("zaky", "current wishlisted item is $it")
            if (it > 0) {
                bottomBadge?.isVisible = true
                bottomBadge?.number = it
            } else {
                bottomBadge?.isVisible = false
            }
        }

        Log.d("zaky", "Current username = ${userPref.getUsername()}")
        binding.Toolbar.title = userPref.getUsername()
    }
}
