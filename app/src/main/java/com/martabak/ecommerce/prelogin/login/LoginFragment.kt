package com.martabak.ecommerce.prelogin.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        if (viewModel.firstEntry) {
            findNavController().navigate(R.id.action_loginFragment_to_onboarding)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingBarBar.isVisible = false
        // content here
        if (viewModel.isLoggedIn()) findNavController().navigate(R.id.action_prelogin_to_postlogin)
        binding.loginButton.isEnabled = false
        binding.registerButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            viewModel.Login()
        }

        viewModel.nowLoading.observe(viewLifecycleOwner) {
            binding.loadingBarBar.isVisible = it
        }

        binding.inputTextEmail.doOnTextChanged { text, start, before, count ->
            var invalidInput = !viewModel.validateEmail(text.toString()) && text.toString().isNotEmpty()
            if (invalidInput) {
                binding.inputTextEmailLayout.isErrorEnabled = true
                binding.inputTextEmailLayout.error = "Invalid Email"
            } else {
                binding.inputTextEmailLayout.isErrorEnabled = false
            }
            binding.loginButton.isEnabled = !invalidInput && viewModel.passwordValidity
        }

        binding.inputTextPassword.doOnTextChanged { text, start, before, count ->
            var invalidInput = !viewModel.validatePassword(text.toString()) && text.toString().isNotEmpty()
            if (invalidInput) {
                binding.inputTextPasswordLayout.isErrorEnabled = true
                binding.inputTextPasswordLayout.error = "Invalid Password"
            } else {
                binding.inputTextPasswordLayout.isErrorEnabled = false
            }
            binding.loginButton.isEnabled = !invalidInput && viewModel.emailValidity
        }

        viewModel.serverValidity.observe(viewLifecycleOwner) {
            if (it) {
                view.findNavController().navigate(R.id.action_prelogin_to_postlogin)
            } else {
                Toast.makeText(activity, viewModel.errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
