package com.infinity.omos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.infinity.omos.R
import com.infinity.omos.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNav()
    }

    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        setTopLevelDestinations(navController)
        setActionBar(navController)
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
        }
    }

    /**
     * Today 제외하고 ActionBar 추가
     */
    private fun setActionBar(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.my_record_fragment,
            )
        )
        // setupActionBarWithNavController(navController, appBarConfiguration)
    }
}