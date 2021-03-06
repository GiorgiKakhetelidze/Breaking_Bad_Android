package com.example.breakingBad.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.base.hideKeyboard
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.databinding.LoginScreenBinding
import com.example.breakingBad.ui.registration.RegistrationFragment
import com.example.breakingBad.utils.observeEvent


class LoginFragment : BaseFragment(), View.OnClickListener {

    private var binding: LoginScreenBinding? = null

    private val viewModel: LoginViewModel by activityViewModels()

    override fun getViewModelInstance() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RegistrationFragment.KEY_DATA) { _, bundle ->
            binding?.userNameInput?.setText(bundle.getString(RegistrationFragment.KEY_USERNAME))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.registerBtnLogin?.setOnClickListener(this)
        binding?.loginButton?.setOnClickListener(this)

        viewModel.inputError.observe(viewLifecycleOwner) {
            binding?.passwordInput?.error = getString(it)
        }
        viewModel.loginSuccess.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        viewModel.loginFragmentStarted()
    }

    override fun onClick(v: View?) {
        hideKeyboard()
        when (v) {
            binding?.registerBtnLogin -> startRegistration()
            binding?.loginButton -> viewModel.login(
                username = binding?.userNameInput?.text,
                password = binding?.passwordInput?.text
            )
        }
    }

    override fun onDestroy() {
        viewModel.loginFragmentDestroyed()
        super.onDestroy()
    }

    private fun startRegistration() {
        findNavController().navigate(R.id.form_login_to_registration)
    }

}