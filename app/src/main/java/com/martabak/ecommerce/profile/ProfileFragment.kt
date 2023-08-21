package com.martabak.ecommerce.profile

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentProfileBinding
import com.martabak.ecommerce.utils.PhotoUriManager


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        //content
        //this needs fileprovider configured on manifest and on the bulidNewUri method.
        val cameraPicUri = PhotoUriManager(requireActivity()).buildNewUri()
        //take le picture
        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                Log.d("zaky", "Photo taking success")
                val inputStream = requireActivity().contentResolver.openInputStream(cameraPicUri)
                val drawable = Drawable.createFromStream(inputStream, cameraPicUri.toString())
                binding.profileOutline.visibility = View.GONE
                binding.profileImage.setImageDrawable(drawable)
            } else {
                Log.d("zaky", "Photo taking semi-success")
            }
        }
        val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
            if (uri != null) {
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                val drawable = Drawable.createFromStream(inputStream, uri.toString())
                binding.profileOutline.visibility = View.GONE
                binding.profileImage.setImageDrawable(drawable)
            }
        }
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
                    //camera
                    if(which == 0) {
                        takePhoto.launch(cameraPicUri)
                        Log.d("zaky", "camera launch executed")

                    } else {
                        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                }
                .show()
        }

        // Inflate the layout for this fragment
        return view
    }


}