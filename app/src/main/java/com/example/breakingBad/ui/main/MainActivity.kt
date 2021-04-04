package com.example.breakingBad.ui.main

import android.os.Bundle
import com.example.breakingBad.base.LanguageAwareActivity
import com.example.breakingBad.databinding.MainActivityBinding


class MainActivity : LanguageAwareActivity() {

    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}