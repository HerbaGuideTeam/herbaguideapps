package com.capstone.herbaguideapps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.capstone.herbaguideapps.databinding.ActivityMainBinding
import com.capstone.herbaguideapps.ui.identify.ModalBottomScanFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}