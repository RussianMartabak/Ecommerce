package com.martabak.ecommerce.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.martabak.ecommerce.R
import com.martabak.ecommerce.checkout.adapters.CheckoutAdapter
import com.martabak.ecommerce.databinding.FragmentCheckoutBinding
import com.martabak.ecommerce.network.data.checkout.CheckoutData
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

//how it works
//get product parcel from other fragment
@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    val args: CheckoutFragmentArgs by navArgs()
    private val viewModel: CheckoutViewModel by viewModels()

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.submitItemList(args.checkoutData.itemList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkoutLoading.isVisible = false
        var adapter = CheckoutAdapter(viewModel)
        binding.confirmBuyButton.isEnabled = false
        viewModel.itemList.observe(viewLifecycleOwner) {
            adapter!!.submitList(it)
            Log.d("zaky", it.toString())
            // change price accordingly
            binding.totalPriceText.text = sumPrice(it)
        }
        binding.checkOutToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
            Log.d("zaky", "navigate up should have happened")
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

        // send parcel
        viewModel.statusParcel.observe(viewLifecycleOwner) {
            val aktion = CheckoutFragmentDirections.startStatusFromCheckout(it)
            findNavController().navigate(aktion)
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
            // Log EVENT
            analytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO) {
                param(FirebaseAnalytics.Param.CURRENCY, "IDR")
                label?.let { param(FirebaseAnalytics.Param.PAYMENT_TYPE, it) }
                param(FirebaseAnalytics.Param.VALUE, viewModel.sum)
            }
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
