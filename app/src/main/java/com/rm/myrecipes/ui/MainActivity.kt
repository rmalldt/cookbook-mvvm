package com.rm.myrecipes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.ActivityMainBinding
import com.rm.myrecipes.ui.utils.extension.hideSystemUi
import com.rm.myrecipes.ui.utils.extension.setGone
import com.rm.myrecipes.ui.utils.extension.setVisible
import com.rm.myrecipes.ui.utils.extension.showSystemUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds =  setOf(
                R.id.recipesFragment,
                R.id.favouriteRecipesFragment,
                R.id.foodJokeFragment
            )
        )

        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.splashFragment -> hideBottomNav()
                R.id.recipesFragment -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        showSystemUi()
        supportActionBar?.show()
        binding.bottomNavigationView.setVisible()
    }

    private fun hideBottomNav() {
        hideSystemUi()
        supportActionBar?.hide()
        binding.bottomNavigationView.setGone()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
