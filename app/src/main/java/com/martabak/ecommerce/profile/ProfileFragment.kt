package com.martabak.ecommerce.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentProfileBinding
import com.martabak.ecommerce.utils.PhotoUriManager
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    cameraPicUri
                )
                val imgFile: File = PhotoUriManager(requireActivity()).uriToFile(cameraPicUri)
                viewModel.selectedFile = imgFile

                binding.profileOutline.visibility = View.GONE
                binding.profileImage.setImageBitmap(bitmap)
            } else {
                Log.d("zaky", "Photo taking semi-success")
            }
        }
        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    val imgFile: File = PhotoUriManager(requireActivity()).uriToFile(uri)
                    viewModel.selectedFile = imgFile
                    //here get the file
                    binding.profileOutline.visibility = View.GONE
                    binding.profileImage.setImageBitmap(bitmap)
                }
            }
        binding.confirmButton.isEnabled = false
        binding.confirmButton.setOnClickListener {
            viewModel.uploadProfile(binding.inputUsername.text.toString())
        }

        viewModel.connectSuccess.observe(viewLifecycleOwner){
            if(it) {
                view.findNavController().navigate(R.id.action_prelogin_to_postlogin)
            } else {
                Toast.makeText(requireActivity(), viewModel.errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
            Log.d("zaky", "connection status change: $it")

        }



        binding.inputUsername.doOnTextChanged { text, start, before, count ->
            binding.confirmButton.isEnabled = text.toString() != ""
        }
        val items = arrayOf("Kamera", "Galeri")
        binding.profileImage.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity()).setTitle("Pilih Gambar")
                .setItems(items) { dialog, which ->
                    //camera
                    if (which == 0) {
                        takePhoto.launch(cameraPicUri)
                        Log.d("zaky", "camera launch executed")

                    } else {
                        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                }.show()
        }

        // Inflate the layout for this fragment
        return view
    }


}