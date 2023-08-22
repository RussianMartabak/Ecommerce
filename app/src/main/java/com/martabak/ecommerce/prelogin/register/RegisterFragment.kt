package com.martabak.ecommerce.prelogin.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    private var validEmail = false
    private var validPass = false

    private fun validateEmail(emailInput: String): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() && emailInput.isNotEmpty()
    }

    private fun validatePassword(passInput: String): Boolean {
        return passInput.length < 8 && passInput.isNotEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.isEnabled = false

        binding.inputEmail.doOnTextChanged { text, _, _, _ ->
            if (!validateEmail(text.toString())) {
                validEmail = true
                binding.inputTextEmailReg.isErrorEnabled = false
            } else {
                validEmail = false
                binding.inputTextEmailReg.error = "Invalid Email"
                binding.inputTextEmailReg.isErrorEnabled = true
            }
            viewModel.email = text.toString()
            checkButton()
        }
        binding.inputPassword.doOnTextChanged { text, _, _, _ ->
            if (!validatePassword(text.toString())) {
                validPass = true
                binding.inputTextPasswordReg.isErrorEnabled = false
            } else {
                validPass = false
                binding.inputTextPasswordReg.error = "Password must be at least 8 characters"
                binding.inputTextPasswordReg.isErrorEnabled = true
            }
            viewModel.password = text.toString()
            checkButton()
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
        }
    }

    private fun checkButton() {
        binding.registerButton.isEnabled = validEmail && validPass
    }
}