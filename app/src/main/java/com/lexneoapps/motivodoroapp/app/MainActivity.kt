package com.lexneoapps.motivodoroapp.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.ActivityMainBinding
import com.lexneoapps.motivodoroapp.other.Constants.ACTION_SHOW_COUNTDOWN_FRAGMENT
import com.lexneoapps.motivodoroapp.other.Constants.ACTION_SHOW_STOPWATCH_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.startFragment,
                R.id.historyFragment,
                R.id.statisticsFragment,
                R.id.quotesFragment
            )
        )

        setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
        navigateToTrackingFragmentIfNeeded(intent)


        /*  navController.addOnDestinationChangedListener{
                  _,destination,_ ->
              when(destination.id){
                  R.id.startFragment, R.id.quotesFragment, R.id.statisticsFragment, R.id.historyFragment ->
                      binding.bottomNavigationView.visibility = View.VISIBLE
                  else -> binding.bottomNavigationView.visibility = View.GONE
              }
          }*/
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_STOPWATCH_FRAGMENT) {
            navController.navigate(R.id.action_global_stopWatchFragment)
        }else if (intent?.action == ACTION_SHOW_COUNTDOWN_FRAGMENT) {
            navController.navigate(R.id.action_global_countDownFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        Timber.i("MAINACTIVITY DESTROYED!!!")
        super.onDestroy()
    }
}