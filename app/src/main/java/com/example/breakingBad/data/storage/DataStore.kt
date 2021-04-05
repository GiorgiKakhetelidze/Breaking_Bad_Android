package com.example.breakingBad.data.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.storage.db.CharacterDb
import java.lang.RuntimeException
import java.util.*

object DataStore {

    private const val KEY_LANGUAGE = "key_language"
    private const val KEY_TOKEN = "key_token"
    private const val KEY_LAST_TIME_SAVED_CARDS_FETCHED = "key_last_time_saved_cards_fetched"

    private var sharedPreferences: SharedPreferences? = null
    private var dataBase: CharacterDb? = null

    val db get() = dataBase ?: throw RuntimeException("not initialized!!")

    fun setSharedPreferences(context: Context, sharedPreferences: SharedPreferences) {
        DataStore.sharedPreferences = sharedPreferences
        dataBase = Room.databaseBuilder(context, CharacterDb::class.java, "charDb").build()
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

    var lastTimeSavedCardsFetched: Long
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences?.edit()?.putLong(KEY_LAST_TIME_SAVED_CARDS_FETCHED, value)?.commit()
        }
        get() {
            return (sharedPreferences ?: throw RuntimeException("not initialized!!"))
                .getLong(KEY_LAST_TIME_SAVED_CARDS_FETCHED, 0)
        }
}