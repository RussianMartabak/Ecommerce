package com.martabak.ecommerce.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentProfileBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.confirmButton.isEnabled = false
        binding.confirmButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_prelogin_to_postlogin)
        }

        binding.inputUsername.doOnTextChanged { text, start, before, count ->
            binding.confirmButton.isEnabled = text.toString() != ""
        }
        val items = arrayOf("Kamera", "Galeri")
        binding.profileImage.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Pilih Gambar")
                .setItems(items) {dialog, which ->


                }
                .show()
        }
        //content
        // Inflate the layout for this fragment
        return view
    }


}