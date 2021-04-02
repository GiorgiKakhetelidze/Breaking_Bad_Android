package com.example.breakingBad.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.databinding.RegistrationScreenBinding

class RegistrationFragment : BaseFragment() {

    private var binding: RegistrationScreenBinding? = null
    private val viewModel by viewModels<RegistrationViewModel>()

    override fun getViewModelInstance() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegistrationScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.backBtn?.setOnClickListener { activity?.onBackPressed() }
        binding?.registerBtnRegister?.setOnClickListener {
            viewModel.onRegister(
                name = binding?.userNameInputFirst?.text,
                username = binding?.userNameInputSecond?.text,
                password = binding?.passwordInputFirst?.text,
                repeatPassword = binding?.passwordInputSecond?.text
            )
        }
        viewModel.validationError.observe(viewLifecycleOwner, this::showValidationError)
        viewModel.registrationComplete.observe(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.loginFragment, true)
        }
    }

    private fun showValidationError(error: RegistrationViewModel.ValidationError) {
        binding?.apply {
            when (error) {
                RegistrationViewModel.ValidationError.EmptyUsername -> {
                    userNameInputFirst.error = getString(R.string.registration_error_empty_username)
                }
                RegistrationViewModel.ValidationError.EmptyName -> {
                    userNameInputSecond.error = getString(R.string.registration_error_empty_name)
                }
                RegistrationViewModel.ValidationError.EmptyPassword -> {
                    passwordInputFirst.error = getString(R.string.registration_error_empty_password)
                }
                RegistrationViewModel.ValidationError.PasswordsNotMatching -> {
                    passwordInputSecond.error =
                        getString(R.string.registration_error_passwords_not_matching)
                }
                RegistrationViewModel.ValidationError.None -> {
                    userNameInputFirst.error = null
                    userNameInputSecond.error = null
                    passwordInputFirst.error = null
                    passwordInputSecond.error = null
                }
            }
        }
    }

    companion object {
        const val KEY_USERNAME = "key_username"
        const val KEY_DATA = "key_password"
    }
}