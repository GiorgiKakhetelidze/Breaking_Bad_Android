package com.example.breakingBad.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.breakingBad.R
import com.example.breakingBad.base.*
import com.example.breakingBad.data.models.user.UserProfile
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.databinding.ProfileScreenBinding
import com.example.breakingBad.ui.dialogs.LanguagePickerBottomSheet
import com.example.breakingBad.ui.login.LoginViewModel
import com.example.breakingBad.utils.observeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProfileFragment : BaseFragment() {

    private var binding: ProfileScreenBinding? = null
    private val viewModel by viewModels<ProfileViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun getViewModelInstance() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.langBtnView?.setOnClickListener {
            val languagePickerBottomSheet = LanguagePickerBottomSheet()
            languagePickerBottomSheet.show(childFragmentManager, "tag")
        }

        binding?.logOutBtnView?.setOnClickListener {
            DataStore.authToken = null
        }

        viewModel.userProfile.observe(viewLifecycleOwner, this::showUserData)
        viewModel.loginRequired.observe(viewLifecycleOwner) {
            activity?.findNavController(R.id.mainContainer)?.navigate(R.id.login)
        }
        loginViewModel.loginFlowFinished.observeEvent(viewLifecycleOwner) { loginSuccess ->
            if (loginSuccess)
                viewModel.getUserData()
            else
                findNavController().navigate(R.id.show_home)
        }
    }

    private fun showUserData(userProfile: UserProfile) {
        binding?.userNameTextView?.text = userProfile.userName
        binding?.nameTextView?.text = userProfile.name
        Glide.with(this@ProfileFragment)
            .load(userProfile.imageUrl)
            .centerCrop()
            .into(binding?.profilePicImgView!!)
    }
}