package com.example.pizzaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pizzaapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            binding.bottomNav.visibility = View.GONE
    val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
    navController = navHostFragment.navController
    navController.addOnDestinationChangedListener { _, destination, _ ->
        if(destination.id == R.id.mainFragment || destination.id == R.id.basketFragment) {
            binding.bottomNav.visibility = View.VISIBLE
            binding.bottomNav.setupWithNavController(navController)
        }
        else {
            binding.bottomNav.visibility = View.GONE
        }
    }
    }
}