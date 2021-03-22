package com.example.breakingBad.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.breakingBad.databinding.RegistrationScreenBinding

class RegistrationFragment : Fragment() {

    private var binding : RegistrationScreenBinding? = null

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
        binding?.registerBtnRegister?.setOnClickListener { returnUsername() }
    }

    private fun returnUsername() {
        val username = binding?.userNameInputFirst?.text.toString()
        setFragmentResult(KEY_DATA, bundleOf(KEY_USERNAME to username))
        parentFragmentManager.popBackStack()
    }

    companion object {
        const val KEY_USERNAME = "key_username"
        const val KEY_DATA = "key_password"
    }
}