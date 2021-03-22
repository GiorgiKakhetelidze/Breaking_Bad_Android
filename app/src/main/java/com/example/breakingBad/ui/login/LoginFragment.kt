package com.example.breakingBad.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.base.hideLoading
import com.example.breakingBad.base.showDialog
import com.example.breakingBad.base.showLoading
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.databinding.LoginScreenBinding
import com.example.breakingBad.ui.registration.RegistrationFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.io.IOException

class LoginFragment : BaseFragment(), View.OnClickListener {

    private var binding: LoginScreenBinding? = null

    private val viewModel : LoginViewModel by activityViewModels()

    override fun getViewModelInstance() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RegistrationFragment.KEY_DATA){_, bundle ->
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
    }

    override fun onClick(v: View?) {
        when (v) {
            binding?.registerBtnLogin -> startRegistration()
            binding?.loginButton -> login()
        }
    }

    private fun login() = lifecycleScope.launchWhenCreated {

        val username = binding?.userNameInput?.text
        val password = binding?.passwordInput?.text
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(
                context,
                getString(R.string.login_toast),
                Toast.LENGTH_SHORT
            ).show()
            return@launchWhenCreated
        }

        showLoading()

        try {
            val response = withContext(Dispatchers.IO) {
                NetworkClient.userService.login(
                    username = username.toString(),
                    password = password.toString()
                )
            }
        } catch (e: IOException) {
            showDialog(
                R.string.common_error,
                e.message ?: getString(R.string.common_unknown_error)
            )
        } finally {
            hideLoading()
        }
    }

    private fun startRegistration() {
        findNavController().navigate(R.id.form_login_to_registration)
    }
}