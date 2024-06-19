package com.capstone.herbaguideapps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.databinding.ActivityMainBinding
import com.capstone.herbaguideapps.session.SessionViewModel
import com.capstone.herbaguideapps.ui.identify.ModalBottomScanFragment
import com.capstone.herbaguideapps.ui.welcome.WelcomeLoginActivity
import com.capstone.herbaguideapps.utlis.factory.SessionViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val sessionViewModel by viewModels<SessionViewModel> {
        SessionViewModelFactory.getInstance(this)
    }

    private val finishReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "finish_main_activity") {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(finishReceiver, IntentFilter("finish_main_activity"))

        val navigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        val navController = navHostFragment.navController

        AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_explore,
                R.id.nav_scan,
                R.id.nav_history,
                R.id.nav_profile
            )
        )

        navigationView.setupWithNavController(navController)

        navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_scan -> {
                    val modalBottomSheet = ModalBottomScanFragment()
                    modalBottomSheet.show(
                        supportFragmentManager,
                        ModalBottomScanFragment::class.java.simpleName
                    )
                    true
                }

                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }

        lifecycleScope.launch {
            sessionViewModel.getSession().observe(this@MainActivity) { session ->
                if (session.isLogin && !session.isGuest) {
                    sessionViewModel.validateToken()
                    sessionViewModel.authResult.observe(this@MainActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {

                                }

                                is Result.Success -> {
                                }

                                is Result.Error -> {

                                    Toast.makeText(
                                        this@MainActivity,
                                        "Sesi sudah berakhir",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    sessionViewModel.logout()
                                    WelcomeLoginActivity.start(this@MainActivity)
                                    finish()
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishReceiver)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}