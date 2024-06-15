package com.capstone.herbaguideapps.ui.welcome

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.capstone.herbaguideapps.MainActivity
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.databinding.ActivityWelcomeLoginBinding
import com.capstone.herbaguideapps.session.SessionModel
import com.capstone.herbaguideapps.ui.welcome.login.LoginActivity
import com.capstone.herbaguideapps.ui.welcome.login.LoginViewModel
import com.capstone.herbaguideapps.ui.welcome.register.RegisterActivity
import com.capstone.herbaguideapps.utlis.factory.AuthViewModelFactory

class WelcomeLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    private val finishReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "finish_welcome_activity") {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(finishReceiver, IntentFilter("finish_welcome_activity"))

        setupAction()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishReceiver)
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                LoginActivity.start(this@WelcomeLoginActivity)
            }

            btnRegister.setOnClickListener {
                RegisterActivity.start(this@WelcomeLoginActivity)
            }

            btnGuest.setOnClickListener {
                loginViewModel.saveSession(
                    SessionModel(
                        "Guest",
                        "",
                        true,
                        "",
                        isGuest = true
                    )
                )

                MainActivity.start(this@WelcomeLoginActivity)
                finish()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WelcomeLoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}