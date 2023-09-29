package com.martabak.ecommerce.main.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentWishlistBinding
import com.martabak.ecommerce.main.wishlist.adapters.WishlistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [WishlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WishlistViewModel by viewModels()
    private var gridMode = false
    private var adapter: WishlistAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.errorLayout.isVisible = false

        viewModel.itemCount.observe(viewLifecycleOwner) {
            binding.totalWishItem.text = "$it barang"
            if (it == 0) {
                binding.errorLayout.isVisible = true
                binding.normallayout.isVisible = false
            } else {
                binding.errorLayout.isVisible = false
                binding.normallayout.isVisible = true
            }
        }

        val grandFrag = (this.requireParentFragment() as NavHostFragment).requireParentFragment()
        adapter = WishlistAdapter(viewModel) {
            viewModel.setSelectedId(it)
            grandFrag.findNavController().navigate(R.id.action_mainFragment_to_productDetailFragment)
        }

        binding.wishRecycler.adapter = adapter
        binding.wishRecycler.layoutManager = GridLayoutManager(requireActivity(), 1)

        viewModel.wishItems.observe(viewLifecycleOwner) {
            adapter!!.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventFlow.collectLatest {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.layoutSelector.setOnClickListener {
            gridMode = !gridMode
            switchLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        switchLayout()
    }

    private fun switchLayout() {
        if (gridMode) {
            binding.layoutSelector.setImageResource(R.drawable.grid_view)
            val gridManager = GridLayoutManager(requireActivity(), 2)
            adapter!!.setGridMode(true)
            binding.wishRecycler.layoutManager = gridManager
            binding.wishRecycler.adapter = adapter
        } else {
            binding.layoutSelector.setImageResource(R.drawable.format_list_bulleted)
            adapter!!.setGridMode(false)
            binding.wishRecycler.layoutManager = GridLayoutManager(requireActivity(), 1)
            binding.wishRecycler.adapter = adapter
        }
    }
}
