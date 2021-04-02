package com.example.breakingBad.ui.navHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.breakingBad.R
import com.example.breakingBad.databinding.NavHomeScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavHomeFragment : Fragment() {

    private var binding: NavHomeScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NavHomeScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.homeNavContainer) as NavHostFragment
        val navController = navHostFragment.navController
        binding?.homeNavTabBar?.setupWithNavController(navController)
    }

}