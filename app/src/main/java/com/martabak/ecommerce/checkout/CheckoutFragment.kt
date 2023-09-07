package com.martabak.ecommerce.checkout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.martabak.ecommerce.R
import com.martabak.ecommerce.checkout.adapters.CheckoutAdapter
import com.martabak.ecommerce.databinding.FragmentCheckoutBinding
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import com.martabak.ecommerce.network.data.checkout.CheckoutList
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat


/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    val args: CheckoutFragmentArgs by navArgs()
    private val viewModel: CheckoutViewModel by viewModels()

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
        var adapter = CheckoutAdapter(viewModel)
        binding.confirmBuyButton.isEnabled = false
        viewModel.itemList.observe(viewLifecycleOwner) {
            adapter!!.submitList(it)
            Log.d("zaky", it.toString())
            //change price accordingly
            binding.totalPriceText.text = sumPrice(it)
        }
        binding.checkOutToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.payment.observe(viewLifecycleOwner) {
            Log.d("zaky", "caught value $it")
            if (it.isNotBlank()) {
                binding.confirmBuyButton.isEnabled = true
            }
        }
        viewModel.nowLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.checkoutLoading.isVisible = true
                binding.confirmBuyButton.isVisible = false
            } else {
                binding.checkoutLoading.isVisible = false
                binding.confirmBuyButton.isVisible = true
            }
        }

        binding.confirmBuyButton.setOnClickListener {
            viewModel.sendFulfillment()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        parentFragmentManager.setFragmentResultListener("paymentMethod", viewLifecycleOwner) {
            requestKey, bundle ->
            val label = bundle.getString("methodName")
            val image = bundle.getString("methodImage")
            binding.apply {
                payCardImage.load(image) {
                    error(R.drawable.thumbnail)
                }
                payCardText.text = label

            }
            viewModel.setPayment(label!!)
        }
        binding.payMethodCard.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentFragment)
        }
        binding.checkoutRecycler.adapter = adapter
        binding.checkoutRecycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.checkOutToolbar.setNavigationOnClickListener {
            findNavController()
        }

    }

    private fun initRecycler() {


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