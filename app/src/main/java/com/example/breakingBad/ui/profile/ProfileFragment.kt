package com.example.breakingBad.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.breakingBad.R
import com.example.breakingBad.base.hideLoading
import com.example.breakingBad.base.showDialog
import com.example.breakingBad.base.showLoading
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.databinding.ProfileScreenBinding
import com.example.breakingBad.ui.dialogs.LanguagePickerBottomSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProfileFragment : Fragment() {

    private val binding by lazy { ProfileScreenBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.langBtnView.setOnClickListener {
            val languagePickerBottomSheet = LanguagePickerBottomSheet()
            languagePickerBottomSheet.show(childFragmentManager, "tag")
        }
        getUserData()
        binding.logOutBtnView.setOnClickListener {
            DataStore.authToken = null
            activity?.onBackPressed()
        }
    }

    private fun getUserData() {
        lifecycleScope.launchWhenCreated {
            showLoading()
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkClient.userService.getUser()
                }
                binding.userNameTextView.text = response.userName
                binding.nameTextView.text = response.name
                Glide.with(this@ProfileFragment)
                    .load(response.imageUrl)
                    .centerCrop()
                    .into(binding.profilePicImgView)

            } catch (e: Exception) {
                when {
                    e is IOException -> {
                        showDialog(R.string.common_error, R.string.common_no_connection)
                    }
                    e is HttpException && e.code() == 403 -> {
                        activity?.findNavController(R.id.mainContainer)
                            ?.navigate(R.id.loginFragment)
                    }
                    else -> showDialog(
                        R.string.common_error,
                        e.message ?: getString(R.string.common_unknown_error)
                    )
                }

            } finally {
                hideLoading()
            }
        }
    }
}