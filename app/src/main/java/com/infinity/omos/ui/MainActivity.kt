package com.infinity.omos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.infinity.omos.R
import com.infinity.omos.databinding.ActivityMainBinding
import com.infinity.omos.utils.offFullScreen
import com.infinity.omos.utils.onFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setGraph(navController)
        setBottomNav(navController)
    }

    private fun setGraph(navController: NavController) {
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        if (viewModel.isExistToken) {
            navGraph.setStartDestination(R.id.nav_main)
        } else {
            navGraph.setStartDestination(R.id.nav_on_boarding)
        }
        navController.graph = navGraph
    }

    private fun setBottomNav(navController: NavController) {
        binding.bottomNav.setupWithNavController(navController)
        setTopLevelDestinations(navController)
    }

    /**
     * Main 화면에서만 BottomNav 노출
     */
    private fun setTopLevelDestinations(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.today_fragment,
                R.id.my_record_fragment,
                R.id.all_records_fragment,
                R.id.my_dj_fragment,
                R.id.my_page_fragment
            )
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopDest = appBarConfiguration.topLevelDestinations.contains(destination.id)
            binding.bottomNav.isVisible = isTopDest

            if (isTopDest) {
                onFullScreen()
            } else {
                offFullScreen()
            }
        }
    }
}