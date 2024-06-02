package com.capstone.herbaguideapps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.capstone.herbaguideapps.databinding.ActivityMainBinding
import com.capstone.herbaguideapps.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isSession = intent.getBooleanExtra(EXTRA_SESSION, false)

        if (!isSession) {
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
            finish()
        }

        val navigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        val navController = navHostFragment.navController

        AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_explore,
                R.id.nav_history
            )
        )

        navigationView.setupWithNavController(navController)
    }

    companion object {
        const val EXTRA_SESSION = "extra_session"

        fun start(context: Context, isLogin: Boolean) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_SESSION, isLogin)
            context.startActivity(intent)
        }
    }
}