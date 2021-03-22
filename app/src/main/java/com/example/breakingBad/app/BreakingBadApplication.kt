package com.example.breakingBad.app

import android.app.Application
import android.content.Context
import com.example.breakingBad.data.storage.DataStore

class BreakingBadApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataStore.setSharedPreferences(getSharedPreferences("_sp_", Context.MODE_PRIVATE))
    }
}