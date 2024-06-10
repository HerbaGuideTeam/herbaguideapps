package com.capstone.herbaguideapps.ui.welcome.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.capstone.herbaguideapps.MainActivity
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.body.LoginBody
import com.capstone.herbaguideapps.databinding.ActivityLoginBinding
import com.capstone.herbaguideapps.ui.welcome.WelcomeLoginActivity
import com.capstone.herbaguideapps.ui.welcome.register.RegisterActivity
import com.capstone.herbaguideapps.utlis.viewmodelfactory.AuthViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this@LoginActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            WelcomeLoginActivity.start(this@LoginActivity)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            RegisterActivity.start(this@LoginActivity)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            val login = LoginBody(
                email, password
            )

            loginViewModel.login(login)
            loginViewModel.loginResult.observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {

                        }

                        is Result.Success -> {

                            setupSession()

                        }

                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                "Login Failed: ${result.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun finishWelcomeActivity() {
        val intent = Intent("finish_welcome_activity")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun setupSession() {
        loginViewModel.getSession().observe(this) { session ->
            if (session.isLogin) {
                finishWelcomeActivity()
                MainActivity.start(this)
                finish()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}