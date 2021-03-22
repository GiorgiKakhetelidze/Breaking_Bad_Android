package com.example.breakingBad.base

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.breakingBad.R

fun Fragment.showDialog(@StringRes title: Int, @StringRes message: Int) {
    showDialog(title, getString(message))
}

fun Fragment.showDialog(@StringRes title: Int, message: String) {
    context?.let {
        AlertDialog.Builder(it)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(
                getString(R.string.common_OK)
            ) { dialog, _ -> dialog.dismiss() }
            .setCancelable(true)
            .show()
    }
}

fun Fragment.showLoading() {
    (activity as? LanguageAwareActivity)?.showLoading()
}

fun Fragment.hideLoading() {
    (activity as? LanguageAwareActivity)?.hideLoading()
}