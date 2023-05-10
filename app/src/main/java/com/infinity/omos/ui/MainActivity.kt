package com.infinity.omos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.infinity.omos.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * onboarding Navigation component 적용을 위한 Activity
 * 추후 MainActivity로 변경 예정
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setBottomNav()
    }
/*
    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        setTopLevelDestinations(navController)

        binding.fab.setOnClickListener {
            navController.navigate(R.id.add_moment_navigation)
        }
    }

    private fun setTopLevelDestinations(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.globe_fragment
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopDest = appBarConfiguration.topLevelDestinations.contains(destination.id)
            binding.bottomAppBar.isVisible = isTopDest
            if (isTopDest) binding.fab.show() else binding.fab.hide()
        }
    }*/
}