package com.martabak.ecommerce.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.martabak.ecommerce.R
import com.martabak.ecommerce.cart.adapters.CartAdapter
import com.martabak.ecommerce.database.CartEntity
import com.martabak.ecommerce.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat


/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CartFragment : Fragment() {

    private val viewModel : CartViewModel by viewModels()

    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.errorLayout.isVisible = false
        binding.deleteSelectedButton.isVisible = false
        binding.buyButton.isVisible = false
        //start display le items
        val cartAdapter = CartAdapter(viewModel)
        viewModel.liveCartItemsList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.normallayout.isVisible = false
                binding.errorLayout.isVisible = true
            } else {
                binding.normallayout.isVisible = true
                binding.errorLayout.isVisible = false
            }
            cartAdapter.submitList(it)
            binding.totalPriceText.text = integerToRupiah(enumPrice(it))
            binding.checkAllButton.isChecked = isAllChecked(it)
        }
        binding.Toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.cartRecycler.adapter = cartAdapter
        binding.cartRecycler.layoutManager = LinearLayoutManager(requireActivity())


        binding.checkAllButton.setOnClickListener {
            if (binding.checkAllButton.isChecked) {
                viewModel.checkAll()
            } else {
                viewModel.uncheckAll()
            }
        }

        binding.deleteSelectedButton.setOnClickListener {
            viewModel.deleteSelected()
        }

        viewModel.someChecked.observe(viewLifecycleOwner) {
            binding.deleteSelectedButton.isVisible = it
            binding.buyButton.isVisible = it

        }


    }
    fun integerToRupiah(value: Int): String {
        var price = NumberFormat.getInstance().format(value).replace(",", ".")
        return "Rp$price"
    }

    private fun enumPrice(list : List<CartEntity>) : Int {
        var priceSum = 0
        list.forEach { item ->
            if (item.isSelected) {
                priceSum += (item.productPrice * item.productQuantity)
            }
        }
        return priceSum
    }

    private fun isAllChecked(list : List<CartEntity>) : Boolean {
        var checkeds = list.filter { it.isSelected }
        return list.size == checkeds.size && list.isNotEmpty()

    }

    private fun anItemSelected(list : List<CartEntity>) : LiveData<Boolean> {
        var checkeds = list.filter { it.isSelected }
        if (checkeds.isEmpty()) {
            return MutableLiveData(false)
        } else {
            return MutableLiveData(true)
        }
    }


}