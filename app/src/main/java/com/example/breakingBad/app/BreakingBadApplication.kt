package com.example.breakingBad.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.breakingBad.R
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.services.MusicService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BreakingBadApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataStore.setSharedPreferences(this, getSharedPreferences("_sp_", Context.MODE_PRIVATE))
       startService(Intent(this, MusicService::class.java))
    }
}