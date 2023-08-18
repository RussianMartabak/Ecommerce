package com.martabak.ecommerce.prelogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.martabak.ecommerce.R
import com.martabak.ecommerce.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        //content here
        binding.loginButton.isEnabled = false
        binding.registerButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_prelogin_to_postlogin)
        }

        binding.inputTextEmail.doOnTextChanged { text, start, before, count ->
            var validInput = viewModel.validateEmail(text.toString())
            if (validInput == false) {
                binding.inputTextEmail.error = "Invalid Email"
            }
            binding.loginButton.isEnabled = validInput && viewModel.passwordValidity
        }

        binding.inputTextPassword.doOnTextChanged { text, start, before, count ->
            var validInput = viewModel.validatePassword(text.toString())
            if (validInput == false) {
                binding.inputTextPassword.error = "Invalid Password"
            }
            binding.loginButton.isEnabled = validInput && viewModel.emailValidity
        }

        viewModel.serverValidity.observe(viewLifecycleOwner) {
            if (it) {
                view.findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            } else {
                Toast.makeText(activity, viewModel.errorMessage, Toast.LENGTH_LONG)
            }
        }

        return view
    }


}