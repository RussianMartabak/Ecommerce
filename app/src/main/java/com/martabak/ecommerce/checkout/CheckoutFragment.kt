package com.martabak.ecommerce.checkout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.martabak.ecommerce.R
import com.martabak.ecommerce.checkout.adapters.CheckoutAdapter
import com.martabak.ecommerce.databinding.FragmentCheckoutBinding
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.network.data.checkout.CheckoutList
import java.text.NumberFormat


/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    val args: CheckoutFragmentArgs by navArgs()
    private val viewModel: CheckoutViewModel by viewModels()
    private var adapter: CheckoutAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.submitItemList(args.checkoutData.itemList)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        viewModel.itemList.observe(viewLifecycleOwner) {
            adapter!!.submitList(it)
            Log.d("zaky", it.toString())
            //change price accordingly
            binding.totalPriceText.text = sumPrice(it)
        }
    }

    private fun initRecycler() {
        adapter = CheckoutAdapter(viewModel)
        binding.checkoutRecycler.adapter = adapter
        binding.checkoutRecycler.layoutManager = LinearLayoutManager(requireActivity())

    }

    private fun sumPrice(list: List<CheckoutData>): String {
        var totalPrice = 0
        list.forEach { data ->
            totalPrice += data.productPrice * data.productQuantity
        }
        val formattedPrice = NumberFormat.getInstance().format(totalPrice).replace(",", ".")
        return "Rp$formattedPrice"
    }


}