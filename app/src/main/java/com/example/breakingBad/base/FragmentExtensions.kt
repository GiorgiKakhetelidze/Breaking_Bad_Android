package com.example.breakingBad.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val windowToken = activity?.currentFocus?.windowToken ?: return
    (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(windowToken, 0)
    }
}