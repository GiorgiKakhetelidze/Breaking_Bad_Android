package com.example.breakingBad.data.storage

import android.annotation.SuppressLint
import android.content.SharedPreferences
import java.lang.RuntimeException
import java.util.*

object DataStore {

    private const val KEY_LANGUAGE = "key_language"
    private const val KEY_TOKEN = "key_token"
    private var sharedPreferences: SharedPreferences? = null

    fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        DataStore.sharedPreferences = sharedPreferences
    }

     var language: String
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences?.edit()?.putString(KEY_LANGUAGE, value)?.commit()
        }
        get() {
            return sharedPreferences?.getString(KEY_LANGUAGE, Locale.getDefault().language)
                ?: throw RuntimeException("not initialized!!")
        }

    var authToken: String?
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences?.edit()?.putString(KEY_TOKEN, value)?.commit()
        }
        get() {
            return (sharedPreferences ?: throw RuntimeException("not initialized!!"))
                .getString(KEY_TOKEN, null)
        }
}