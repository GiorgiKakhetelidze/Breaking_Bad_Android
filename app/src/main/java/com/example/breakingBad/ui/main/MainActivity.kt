package com.example.breakingBad.ui.main

import android.content.Intent
import android.os.Bundle
import com.example.breakingBad.base.LanguageAwareActivity
import com.example.breakingBad.databinding.MainActivityBinding
import com.example.breakingBad.services.MusicService


class MainActivity : LanguageAwareActivity() {

    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startService(Intent(this, MusicService::class.java))
    }

    override fun onDestroy() {
        stopService(Intent(this, MusicService::class.java))
        super.onDestroy()
    }
}