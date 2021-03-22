package com.example.breakingBad.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.breakingBad.R
import com.example.breakingBad.utils.observeEvent

abstract class BaseFragment : Fragment() {

    protected abstract fun getViewModelInstance(): BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModelInstance().loading.observe(viewLifecycleOwner) {
            if (it) showLoading()
            else hideLoading()
        }

        getViewModelInstance().dialog.observeEvent(lifecycleOwner = viewLifecycleOwner) {
            if (!it.message.isNullOrEmpty()) {
                showDialog(it.title, it.message)
                return@observeEvent
            }
            if (it.messageRes != null) {
                showDialog(it.title, it.messageRes)
            } else {
                showDialog(it.title, R.string.common_unknown_error)
            }
        }

    }
}