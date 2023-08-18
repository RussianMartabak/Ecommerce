package com.martabak.ecommerce.prelogin

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel : RegisterViewModel by viewModels()



    fun validateEmail(emailInput : String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
    }

    fun validatePassword(passInput : String) : Boolean {
        return passInput.length >= 8
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.registerButton.isEnabled = false
        //content here
        var validEmail = false
        var validPass = false
        fun checkButton() {
            binding.registerButton.isEnabled = validEmail && validPass
        }
        binding.inputEmail.doOnTextChanged { text, start, before, count ->
            if (validateEmail(text.toString())) {
                validEmail = true

                checkButton()
            } else {
                validEmail = false
                binding.inputEmail.error = "Invalid Email"
                checkButton()
            }
            viewModel.email = text.toString()

        }
        binding.inputPassword.doOnTextChanged { text, start, before, count ->
            if (validatePassword(text.toString())) {
                validPass = true
                checkButton()
            } else {
                validPass = false
                binding.inputPassword.error = "Password must be at least 8 characters"
                checkButton()
            }
            viewModel.password = text.toString()
        }

        //observe if register successful
        viewModel.validity.observe(viewLifecycleOwner) {
            if (it) {
                view.findNavController().navigate(R.id.action_registerFragment_to_profileFragment)
            } else {
                Toast.makeText(activity, viewModel.errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.loginButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {
            viewModel.register()
            //
        }



        // Inflate the layout for this fragment
        return view
    }


}