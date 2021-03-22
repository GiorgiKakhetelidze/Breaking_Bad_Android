package com.example.breakingBad.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ContentFrameLayout
import com.example.breakingBad.R
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.utils.updateLocale

abstract class LanguageAwareActivity : AppCompatActivity() {

    private lateinit var loadingView: View

    private lateinit var contentView: ContentFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentView = findViewById<ContentFrameLayout>(android.R.id.content)
        loadingView = layoutInflater.inflate(R.layout.dialog_loading, contentView, false)
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLangContext = newBase?.let { updateLocale(it, DataStore.language) }
        super.attachBaseContext(newLangContext)
    }

    protected fun showDialog(@StringRes title: Int, @StringRes message: Int) {
        showDialog(title, getString(message))
    }

    private fun showDialog(@StringRes title: Int, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(
                getString(R.string.common_OK)
            ) { dialog, _ -> dialog.dismiss() }
            .setCancelable(true)
            .show()
    }
    fun showLoading() {
        contentView.addView(loadingView)
    }

    fun hideLoading() {
        contentView.removeView(loadingView)
    }
}