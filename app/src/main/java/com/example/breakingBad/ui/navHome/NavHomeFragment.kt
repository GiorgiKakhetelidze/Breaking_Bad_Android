package com.example.breakingBad.ui.navHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.breakingBad.R
import com.example.breakingBad.databinding.NavHomeScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavHomeFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

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
        binding?.homeNavTabBar?.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val controller = Navigation.findNavController(binding?.homeNavContainer!!)
        when (item.itemId) {
            R.id.home -> {
                controller.navigate(R.id.show_home)
            }
            R.id.search -> {
                controller.navigate(R.id.show_search)
            }
            R.id.saved -> {
            }

            R.id.profile -> {
                controller.navigate(R.id.show_profile)
            }
        }
        return true
    }
}