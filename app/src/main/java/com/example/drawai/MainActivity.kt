package com.example.drawai

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.drawai.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupToolbar()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Настройка BottomNavigationView
        binding.bottomNav.setupWithNavController(navController)

        // Скрываем BottomNav на некоторых экранах
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.generationFragment, R.id.artDetailFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}