package com.martabak.ecommerce.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.martabak.ecommerce.checkout.adapters.PaymentParentAdapter
import com.martabak.ecommerce.databinding.FragmentPaymentBinding
import com.martabak.ecommerce.utils.GlobalUtils.toPaymentResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PaymentViewModel by viewModels()

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        viewModel.getPaymentData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PaymentParentAdapter { label, image ->
            setFragmentResult(
                "paymentMethod",
                bundleOf(
                    "methodName" to label,
                    "methodImage" to image
                )
            )
            findNavController().navigateUp()
        }

        binding.paymentToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.paymentRecyclerParent.apply {
            setAdapter(adapter)
            layoutManager = LinearLayoutManager(requireActivity())
        }

        initConfig(adapter)

        Log.d("zaky", "Catch a data : ${viewModel.remoteConfig.getString("remoteconfig_data")}")
    }

    private fun initConfig(adapter: PaymentParentAdapter) {
        remoteConfig.fetchAndActivate().addOnCompleteListener(requireActivity()) { task ->
            binding.loadingCircle.isVisible = false
            if (task.isSuccessful) {
                val updated = task.result
                val config = remoteConfig.getString("remoteconfig_data")
                adapter.submitList(config.toPaymentResponse().data)
            }
        }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("remoteconfig_data")) {
                    remoteConfig.activate().addOnCompleteListener {
                        val config = remoteConfig.getString("remoteconfig_data")
                        adapter.submitList(config.toPaymentResponse().data)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w("zaky", "Config update error with code: " + error.code, error)
            }
        })
    }
}
