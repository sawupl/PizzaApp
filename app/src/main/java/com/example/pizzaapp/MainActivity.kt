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
//    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        navControllerAuth.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.loginFragment, R.id.registrationFragment -> {
//                    // Скрыть Bottom Navigation Bar при нахождении в фрагментах авторизации
//                    findViewById<BottomNavigationView>(R.id.bottom_nav_view).visibility = View.GONE
//                }
//                else -> {
//                    // Показать Bottom Navigation Bar при нахождении в других фрагментах
//                    findViewById<BottomNavigationView>(R.id.bottom_nav_view).visibility = View.VISIBLE
//                }
//            }
//        }
            binding.bottomNav.visibility = View.GONE
    val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
    navController = navHostFragment.navController
    navController.addOnDestinationChangedListener { _, destination, _ ->
        if(destination.id == R.id.mainFragment) {
            binding.bottomNav.visibility = View.VISIBLE
            binding.bottomNav.setupWithNavController(navController)
        }
    }



//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
//        bottomNavigationView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
//        appBarConfiguration = AppBarConfiguration(
//            setOf(R.id.titleScreen, R.id.leaderboard,  R.id.register)
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration)
//    }

}